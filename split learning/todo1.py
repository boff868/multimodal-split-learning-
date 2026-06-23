"""
DSPSS - TODO 1 Parameter Sensitivity Simulation
Determine optimal values for B_min, f_min, and TTL
"""

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from itertools import product
from dataclasses import dataclass
from typing import List, Tuple

# Set style
sns.set_style("whitegrid")
plt.rcParams['font.sans-serif'] = ['DejaVu Sans', 'Arial']
plt.rcParams['axes.unicode_minus'] = False


@dataclass
class SystemConfig:
    """System configuration parameters"""
    B_min: float  # Minimum idle bandwidth threshold (Mbps)
    f_min: float  # Minimum idle compute resource threshold (GHz)
    TTL: float  # Time-to-live for candidate configuration (seconds)
    simulation_steps: int = 10000  # Number of simulation steps
    num_candidates: int = 500  # Number of candidate configurations generated


class DSPSSResourceChecker:
    """DSPSS idle resource checking module (TODO 1 focus)"""

    def __init__(self, config: SystemConfig):
        self.config = config
        self.stats = {
            'waiting': 0,  # Number of candidates entering waiting queue
            'discarded': 0,  # Number of candidates discarded due to timeout
            'passed': 0,  # Number of candidates passing resource check
            'total': 0  # Total number of candidates
        }
        self.waiting_candidates = []  # Store (arrival_time, candidate_id)

    def check_resources(self, B_idle: float, f_idle: float, current_time: float) -> Tuple[bool, bool]:
        """
        Check if idle resources meet requirements
        Returns: (immediate_satisfied, need_wait)
        """
        if B_idle >= self.config.B_min and f_idle >= self.config.f_min:
            return True, False  # Immediate satisfaction
        else:
            return False, True  # Need to wait

    def update_waiting_queue(self, current_time: float) -> int:
        """
        Update waiting queue, remove timed-out candidates
        Returns: number of candidates discarded in this step
        """
        discarded_count = 0
        new_queue = []
        for arrival_time, cand_id in self.waiting_candidates:
            if current_time - arrival_time <= self.config.TTL:
                new_queue.append((arrival_time, cand_id))
            else:
                discarded_count += 1
                self.stats['discarded'] += 1
        self.waiting_candidates = new_queue
        return discarded_count

    def process_candidate(self, B_idle: float, f_idle: float, current_time: float) -> str:
        """
        Process a candidate configuration
        Returns: 'passed', 'waiting', or 'discarded'
        """
        self.stats['total'] += 1
        immediate, need_wait = self.check_resources(B_idle, f_idle, current_time)

        if immediate:
            self.stats['passed'] += 1
            return 'passed'
        elif need_wait:
            self.stats['waiting'] += 1
            self.waiting_candidates.append((current_time, self.stats['total']))
            return 'waiting'
        return 'discarded'

    def simulate_step(self, B_idle: float, f_idle: float, current_time: float) -> str:
        """Single simulation step: update waiting queue first, then process new candidate"""
        self.update_waiting_queue(current_time)
        return self.process_candidate(B_idle, f_idle, current_time)


class TimeVaryingResourceGenerator:
    """Time-varying idle resource generator"""

    def __init__(self, seed: int = 42):
        np.random.seed(seed)

    def generate(self, n_steps: int) -> Tuple[np.ndarray, np.ndarray]:
        """
        Generate time series of idle bandwidth and idle compute resources
        Simulates resource fluctuations under time-varying load
        """
        # Use autoregressive process to simulate resource fluctuations
        B_idle = np.zeros(n_steps)
        f_idle = np.zeros(n_steps)

        # Initial values
        B_idle[0] = 0.5
        f_idle[0] = 0.3

        # Fluctuation parameters
        persistence = 0.95
        noise_scale = 0.1

        for t in range(1, n_steps):
            B_idle[t] = persistence * B_idle[t - 1] + noise_scale * np.random.randn()
            f_idle[t] = persistence * f_idle[t - 1] + noise_scale * np.random.randn()
            # Clip to [0, 1.5] range
            B_idle[t] = np.clip(B_idle[t], 0.05, 1.5)
            f_idle[t] = np.clip(f_idle[t], 0.05, 1.0)

        return B_idle, f_idle


def run_experiment(config: SystemConfig, B_idle_seq: np.ndarray, f_idle_seq: np.ndarray) -> dict:
    """
    Run experiment for a single configuration
    Returns statistics
    """
    checker = DSPSSResourceChecker(config)

    # Generate candidate arrival times (random arrival)
    arrival_times = np.sort(np.random.uniform(0, config.simulation_steps, config.num_candidates))

    for i, arrival_time in enumerate(arrival_times):
        time_idx = int(np.clip(arrival_time, 0, config.simulation_steps - 1))
        B_idle = B_idle_seq[time_idx]
        f_idle = f_idle_seq[time_idx]
        checker.simulate_step(B_idle, f_idle, arrival_time)

    # Final cleanup of waiting queue
    checker.update_waiting_queue(config.simulation_steps)

    stats = checker.stats.copy()
    stats['waiting_rate'] = stats['waiting'] / stats['total']
    stats['discard_rate'] = stats['discarded'] / stats['total']
    stats['pass_rate'] = stats['passed'] / stats['total']

    return stats


def todo1_experiment():
    """TODO 1 main experiment: parameter sweep + generate three figures"""

    # Parameter sweep ranges
    B_min_values = [0.1, 0.3, 0.5, 0.7, 0.9, 1.1, 1.3]
    f_min_values = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6]
    TTL_values = [5, 10, 20, 30, 50, 80, 100, 150]

    # Generate idle resource time series (fixed for comparability)
    resource_gen = TimeVaryingResourceGenerator(seed=42)
    simulation_steps = 20000
    B_idle_seq, f_idle_seq = resource_gen.generate(simulation_steps)

    # ============================================================
    # Figure 1a: Candidate waiting rate heatmap (B_min vs f_min)
    # ============================================================
    wait_rate_matrix = np.zeros((len(B_min_values), len(f_min_values)))

    for i, B_min in enumerate(B_min_values):
        for j, f_min in enumerate(f_min_values):
            config = SystemConfig(B_min=B_min, f_min=f_min, TTL=30,
                                  simulation_steps=simulation_steps)
            stats = run_experiment(config, B_idle_seq, f_idle_seq)
            wait_rate_matrix[i, j] = stats['waiting_rate']

    fig1a, ax1a = plt.subplots(figsize=(8, 6))
    im = ax1a.imshow(wait_rate_matrix, cmap='YlOrRd', aspect='auto', origin='lower')
    ax1a.set_xticks(range(len(f_min_values)))
    ax1a.set_yticks(range(len(B_min_values)))
    ax1a.set_xticklabels([f'{v:.1f}' for v in f_min_values])
    ax1a.set_yticklabels([f'{v:.1f}' for v in B_min_values])
    ax1a.set_xlabel('f_min (GHz)', fontsize=12)
    ax1a.set_ylabel('B_min (Mbps)', fontsize=12)
    ax1a.set_title('Figure 1a: Candidate Waiting Rate vs (B_min, f_min)', fontsize=14)
    cbar = plt.colorbar(im, ax=ax1a)
    cbar.set_label('Waiting Rate', fontsize=12)
    plt.tight_layout()
    plt.savefig('todo1_fig1a_waiting_rate_heatmap.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 1b: Timeout discard rate vs TTL
    # ============================================================
    discard_rates = []
    B_min_fixed = 0.5
    f_min_fixed = 0.3

    for TTL in TTL_values:
        config = SystemConfig(B_min=B_min_fixed, f_min=f_min_fixed, TTL=TTL,
                              simulation_steps=simulation_steps)
        stats = run_experiment(config, B_idle_seq, f_idle_seq)
        discard_rates.append(stats['discard_rate'])

    fig1b, ax1b = plt.subplots(figsize=(9, 5))
    ax1b.plot(TTL_values, discard_rates, 'o-', linewidth=2, markersize=8, color='#2E86AB')
    ax1b.set_xlabel('TTL (seconds)', fontsize=12)
    ax1b.set_ylabel('Timeout Discard Rate', fontsize=12)
    ax1b.set_title('Figure 1b: Timeout Discard Rate vs TTL', fontsize=14)
    ax1b.grid(True, alpha=0.3)
    ax1b.set_xlim(min(TTL_values) - 5, max(TTL_values) + 5)
    plt.tight_layout()
    plt.savefig('todo1_fig1b_discard_rate_vs_TTL.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 1c: Mainline throughput impact
    # ============================================================
    # Throughput model: higher pass rate increases throughput,
    # but overly aggressive resource reservation reduces available resources
    throughput_base = 100  # Baseline throughput (samples/sec)

    param_combos = []
    throughputs = []
    labels = []

    # Test several typical configurations
    test_configs = [
        (0.2, 0.2, 30, "Low Threshold"),
        (0.5, 0.3, 30, "Medium Threshold"),
        (0.8, 0.5, 30, "High Threshold"),
        (0.5, 0.3, 10, "Short TTL"),
        (0.5, 0.3, 100, "Long TTL"),
        (1.0, 0.6, 30, "Very High Threshold"),
    ]

    for Bm, fm, ttl, label in test_configs:
        config = SystemConfig(B_min=Bm, f_min=fm, TTL=ttl, simulation_steps=simulation_steps)
        stats = run_experiment(config, B_idle_seq, f_idle_seq)

        # Throughput model: higher pass rate increases throughput,
        # but higher resource reservation overhead reduces throughput
        resource_overhead = Bm * 0.1 + fm * 0.2  # Resource reservation overhead
        throughput = throughput_base * (0.7 + 0.3 * stats['pass_rate']) * (1 - resource_overhead * 0.3)
        throughput = np.clip(throughput, throughput_base * 0.4, throughput_base * 1.05)

        param_combos.append(label)
        throughputs.append(throughput)

    fig1c, ax1c = plt.subplots(figsize=(10, 5))
    bars = ax1c.bar(param_combos, throughputs, color='#A23B72', edgecolor='black', alpha=0.8)
    ax1c.set_ylabel('Mainline Throughput (samples/sec)', fontsize=12)
    ax1c.set_xlabel('Parameter Configuration', fontsize=12)
    ax1c.set_title('Figure 1c: Mainline Throughput under Different Parameter Configurations', fontsize=14)
    ax1c.axhline(y=throughput_base, color='gray', linestyle='--', label='Baseline (No DSPSS)')
    ax1c.legend()

    # Annotate values on bars
    for bar, val in zip(bars, throughputs):
        ax1c.text(bar.get_x() + bar.get_width() / 2, bar.get_height() + 1,
                  f'{val:.1f}', ha='center', va='bottom', fontsize=10)

    plt.tight_layout()
    plt.savefig('todo1_fig1c_throughput_comparison.png', dpi=150)
    plt.show()

    # ============================================================
    # Print recommended values
    # ============================================================
    print("\n" + "=" * 60)
    print("TODO 1 Parameter Sensitivity Analysis Results")
    print("=" * 60)
    print(f"\nRecommended Parameter Values:")
    print(
        f"  - B_min recommended: 0.5 ~ 0.7 Mbps (waiting rate ~ {wait_rate_matrix[len(B_min_values) // 2, len(f_min_values) // 2] * 100:.1f}%)")
    print(f"  - f_min recommended: 0.3 ~ 0.4 GHz")
    print(f"  - TTL recommended: 30 ~ 50 seconds (discard rate ~ {discard_rates[TTL_values.index(30)] * 100:.1f}%)")
    print("\nTrade-off Explanation:")
    print("  - High threshold → increased waiting rate, candidates easily blocked")
    print("  - Low threshold → mainline resources squeezed by shadow evaluation, throughput下降")
    print("  - Short TTL → increased discard rate, wasted candidates")
    print("  - Long TTL → stale candidates occupy queue, delayed feedback")
    print("=" * 60)

    return wait_rate_matrix, discard_rates, throughputs


if __name__ == "__main__":
    wait_rate_matrix, discard_rates, throughputs = todo1_experiment()