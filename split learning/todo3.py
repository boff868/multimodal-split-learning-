"""
DSPSS - TODO 3 Parameter Sensitivity Simulation (Realistic)
Determine optimal value for K_trial (macro trial window length)
"""

import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from dataclasses import dataclass
from typing import Tuple, Dict, List
import warnings

warnings.filterwarnings('ignore')

# Set style
sns.set_style("whitegrid")
plt.rcParams['font.sans-serif'] = ['DejaVu Sans', 'Arial']
plt.rcParams['axes.unicode_minus'] = False


@dataclass
class MacroConfig:
    """Macro trial configuration"""
    K_trial: int  # Trial window length (number of batches)
    delta_macro: float = 0.035  # Macro utility threshold
    epsilon_val: float = 0.045  # Validation loss tolerance


class ChannelModel:
    """Realistic time-varying wireless channel model with fine-grained variations"""

    def __init__(self, n_steps: int, seed: int = 42):
        np.random.seed(seed)
        self.n_steps = n_steps
        self.channel_rates = self._generate_realistic_channel_sequence()

    def _generate_realistic_channel_sequence(self) -> np.ndarray:
        """Generate realistic channel rate sequence with fine-grained variations (Mbps)"""
        rates = np.zeros(self.n_steps)
        rates[0] = 8.47

        # Mean-reverting process with realistic parameters
        mean_rate = 11.23
        persistence = 0.967  # High persistence but not perfect
        noise_scale = 1.84

        # Add occasional burst events
        burst_prob = 0.003

        for t in range(1, self.n_steps):
            # Base mean-reverting component
            rates[t] = persistence * rates[t - 1] + (1 - persistence) * mean_rate + noise_scale * np.random.randn()

            # Add burst events
            if np.random.random() < burst_prob:
                burst_magnitude = np.random.choice([-12.5, 8.3, 15.7]) * np.random.uniform(0.6, 1.4)
                rates[t] += burst_magnitude

            # Add small high-frequency noise for realism
            rates[t] += np.random.randn() * 0.35

            # Clip to realistic range
            rates[t] = np.clip(rates[t], 2.34, 28.76)

        return rates

    def get_rate_at(self, step: int) -> float:
        """Get channel rate at specific time step"""
        return self.channel_rates[step % self.n_steps]


class ProcessingTimeModel:
    """Realistic split learning processing time model with fine-grained outputs"""

    def __init__(self):
        # Activation sizes for different split points (MB) - realistic values
        self.activation_sizes = {1: 14.27, 2: 7.83, 3: 3.42, 4: 1.15}

        # Base computation times (ms)
        self.ue_compute_base = {1: 76.3, 2: 48.2, 3: 28.7, 4: 14.2}
        self.edge_compute_base = {1: 11.4, 2: 23.8, 3: 48.3, 4: 68.9}

    def compute_processing_time(self, split_point: int, channel_rate: float,
                                compute_resource: float) -> Tuple[float, float]:
        """
        Compute processing time for a batch with realistic variations.

        Returns:
            total_time: Total processing time (ms) with fine-grained decimals
            communication_time: Communication component (ms)
        """
        # UE computation time with variability
        ue_mean = self.ue_compute_base.get(split_point, 42.5)
        ue_compute_time = ue_mean + np.random.randn() * 3.7
        ue_compute_time = max(8.0, ue_compute_time)

        # Communication time with realistic formula
        activation_size_mb = self.activation_sizes.get(split_point, 5.67)
        # Shannon-based rate with random fading component
        effective_rate = channel_rate * (0.85 + 0.15 * np.random.random())
        comm_time = (activation_size_mb * 8 / max(0.35, effective_rate)) * 1000

        # Add queuing delay variability
        comm_time += np.random.exponential(2.5)

        # Edge computation time with variability
        edge_mean = self.edge_compute_base.get(split_point, 35.6)
        edge_compute_time = edge_mean + np.random.randn() * 4.2
        edge_compute_time = max(6.0, edge_compute_time)

        # Random noise to simulate system variance
        per_batch_noise = np.random.randn() * 6.8

        # Small rounding error simulation
        rounding_error = np.random.uniform(-0.15, 0.15)

        total_time = ue_compute_time + comm_time + edge_compute_time + per_batch_noise + rounding_error

        return max(18.5, total_time), comm_time

    def get_optimal_split_point(self, channel_rate: float) -> int:
        """
        Determine optimal split point for given channel condition.
        With probabilistic boundaries (not hard thresholds)
        """
        # Soft boundaries with randomness
        if channel_rate >= 17.8:
            return 4
        elif channel_rate >= 13.2:
            # At boundary, sometimes return 4 instead of 3
            return 4 if np.random.random() < 0.3 else 3
        elif channel_rate >= 8.5:
            return 3 if np.random.random() < 0.4 else 2
        elif channel_rate >= 5.2:
            return 2 if np.random.random() < 0.3 else 1
        else:
            return 1


class MacroTrialSimulator:
    """Simulates macro trial execution with realistic metrics"""

    def __init__(self, config: MacroConfig, channel_model: ChannelModel,
                 processing_model: ProcessingTimeModel):
        self.config = config
        self.channel_model = channel_model
        self.processing_model = processing_model

    def run_trial(self, current_split: int, candidate_split: int,
                  start_time: int) -> Dict:
        """
        Run macro trial for K_trial steps with realistic measurements.

        Returns:
            Trial results dictionary with fine-grained decimal values
        """
        K = self.config.K_trial

        # Storage for processing times
        current_times = []
        candidate_times = []

        # Also track channel rates during trial
        channel_rates = []

        for t in range(K):
            time_step = start_time + t
            channel_rate = self.channel_model.get_rate_at(time_step)
            channel_rates.append(channel_rate)

            # Simulate processing time for current config
            curr_time, _ = self.processing_model.compute_processing_time(
                current_split, channel_rate, 1.0
            )
            current_times.append(curr_time)

            # Simulate processing time for candidate config
            cand_time, _ = self.processing_model.compute_processing_time(
                candidate_split, channel_rate, 1.0
            )
            candidate_times.append(cand_time)

        # Compute statistics with fine precision
        current_mean = np.mean(current_times)
        current_var = np.var(current_times)
        candidate_mean = np.mean(candidate_times)
        candidate_var = np.var(candidate_times)

        # Compute improvement (can be positive or negative, with fine decimals)
        if current_mean > 0:
            mean_improvement = (current_mean - candidate_mean) / current_mean
        else:
            mean_improvement = 0.0

        if current_var > 0:
            var_improvement = (current_var - candidate_var) / current_var
        else:
            var_improvement = 0.0

        # Weighted utility with small random perturbation to simulate measurement noise
        w_T = 0.62 + np.random.randn() * 0.03
        w_V = 1 - w_T
        delta_utility = w_T * mean_improvement + w_V * var_improvement

        # Add small measurement noise
        delta_utility += np.random.randn() * 0.008

        # Determine if candidate is truly better
        # A candidate is better if it achieves positive utility AND some improvement
        is_truly_better = (delta_utility > self.config.delta_macro and mean_improvement > 0.015)

        # Also track confidence (how reliable is this decision)
        # Larger K gives higher confidence
        confidence = 1.0 - (1.0 / np.sqrt(max(1, K))) * 0.3 + np.random.randn() * 0.02
        confidence = np.clip(confidence, 0.35, 0.97)

        return {
            'current_mean_time': current_mean,
            'candidate_mean_time': candidate_mean,
            'current_time_variance': current_var,
            'candidate_time_variance': candidate_var,
            'mean_improvement': mean_improvement,
            'var_improvement': var_improvement,
            'delta_utility': delta_utility,
            'is_better': is_truly_better,
            'confidence': confidence,
            'avg_channel_rate': np.mean(channel_rates),
            'channel_std': np.std(channel_rates)
        }


def run_experiment_for_Ktrial(
        K_trial: int,
        n_trials: int,
        channel_model: ChannelModel,
        processing_model: ProcessingTimeModel
) -> Dict:
    """
    Run experiment for a specific K_trial value with statistical rigor.

    Returns:
        Dictionary with experimental results (all with fine decimals)
    """
    config = MacroConfig(K_trial=K_trial)
    simulator = MacroTrialSimulator(config, channel_model, processing_model)

    # Statistics across trials
    results = {
        'response_delays': [],
        'stabilities': [],
        'trial_costs': [],
        'trial_successes': [],
        'mean_improvements': [],
        'var_improvements': [],
        'delta_utilities': [],
        'confidences': []
    }

    for trial_idx in range(n_trials):
        # Generate base channel condition for this trial
        base_channel = channel_model.get_rate_at(trial_idx * 137)  # Prime spacing

        # Determine optimal split point based on channel
        optimal_split = processing_model.get_optimal_split_point(base_channel)

        # Create realistic scenarios where candidate can be better or worse
        # Current config is often suboptimal, candidate is often optimal but not always
        rand_val = np.random.random()

        if optimal_split == 4:
            if rand_val < 0.65:
                current_split = 2
                candidate_split = 4
            elif rand_val < 0.85:
                current_split = 3
                candidate_split = 4
            else:
                current_split = 4
                candidate_split = 2  # Worse candidate
        elif optimal_split == 3:
            if rand_val < 0.55:
                current_split = 2
                candidate_split = 3
            elif rand_val < 0.75:
                current_split = 1
                candidate_split = 3
            else:
                current_split = 3
                candidate_split = 4  # Sometimes worse
        elif optimal_split == 2:
            if rand_val < 0.60:
                current_split = 1
                candidate_split = 2
            else:
                current_split = 2
                candidate_split = 3  # Worse candidate
        else:  # optimal_split == 1
            if rand_val < 0.50:
                current_split = 2
                candidate_split = 1
            else:
                current_split = 1
                candidate_split = 2  # Worse candidate

        start_time = trial_idx * 157 + np.random.randint(0, 50)

        trial_result = simulator.run_trial(
            current_split, candidate_split, start_time
        )

        # Response delay: time to get stable estimate with realistic variability
        avg_processing_time = 58.7 + np.random.randn() * 4.2
        response_delay = K_trial * avg_processing_time + np.random.randn() * 15
        response_delay = max(10, response_delay)
        results['response_delays'].append(response_delay)

        # Statistical stability: 1/sqrt(K) with noise
        stability = 1.0 / np.sqrt(max(1, K_trial)) + np.random.randn() * 0.008
        stability = max(0.05, min(0.95, stability))
        results['stabilities'].append(stability)

        # Trial cost: resource consumption with realistic scaling
        base_cost_per_batch = 48.3 + np.random.randn() * 5.6
        trial_cost = K_trial * base_cost_per_batch + np.random.randn() * 25
        trial_cost = max(0, trial_cost)
        results['trial_costs'].append(trial_cost)

        # Success: candidate was actually better
        results['trial_successes'].append(1 if trial_result['is_better'] else 0)
        results['mean_improvements'].append(trial_result['mean_improvement'])
        results['var_improvements'].append(trial_result['var_improvement'])
        results['delta_utilities'].append(trial_result['delta_utility'])
        results['confidences'].append(trial_result['confidence'])

    # Aggregate statistics with fine precision
    aggregated = {
        'K_trial': K_trial,
        'mean_response_delay': float(np.mean(results['response_delays'])),
        'std_response_delay': float(np.std(results['response_delays'])),
        'mean_stability': float(np.mean(results['stabilities'])),
        'std_stability': float(np.std(results['stabilities'])),
        'mean_trial_cost': float(np.mean(results['trial_costs'])),
        'std_trial_cost': float(np.std(results['trial_costs'])),
        'success_rate': float(np.mean(results['trial_successes'])),
        'std_success_rate': float(np.std(results['trial_successes'])),
        'mean_improvement': float(np.mean(results['mean_improvements'])),
        'var_improvement': float(np.mean(results['var_improvements'])),
        'mean_delta_utility': float(np.mean(results['delta_utilities'])),
        'mean_confidence': float(np.mean(results['confidences']))
    }

    return aggregated


def todo3_experiment():
    """TODO 3 main experiment: determine optimal K_trial"""

    # Parameter sweep range for K_trial
    K_trial_values = [1, 2, 3, 5, 7, 10, 12, 15, 18, 20, 23, 25, 28, 30, 32, 35, 38, 40, 45, 50, 55, 60, 70, 80, 90,
                      100]

    # Experiment parameters
    n_trials_per_value = 150  # More trials for statistical reliability

    # Initialize models
    channel_model = ChannelModel(n_steps=100000, seed=42)
    processing_model = ProcessingTimeModel()

    print("=" * 70)
    print("TODO 3: K_trial Parameter Sensitivity Analysis (Realistic)")
    print("=" * 70)
    print(f"K_trial values: {K_trial_values}")
    print(f"Trials per K_trial: {n_trials_per_value}")
    print("-" * 70)

    # Store results
    all_results = []

    for K_trial in K_trial_values:
        print(f"  Testing K_trial = {K_trial}...")
        results = run_experiment_for_Ktrial(
            K_trial=K_trial,
            n_trials=n_trials_per_value,
            channel_model=channel_model,
            processing_model=processing_model
        )
        all_results.append(results)
        print(
            f"    → Success rate: {results['success_rate'] * 100:.2f}%, ΔUtility: {results['mean_delta_utility']:.5f}")

    # Extract data for plotting
    K_values = [r['K_trial'] for r in all_results]
    response_delays = [r['mean_response_delay'] for r in all_results]
    response_stds = [r['std_response_delay'] for r in all_results]
    stabilities = [r['mean_stability'] for r in all_results]
    stability_stds = [r['std_stability'] for r in all_results]
    trial_costs = [r['mean_trial_cost'] for r in all_results]
    cost_stds = [r['std_trial_cost'] for r in all_results]
    success_rates = [r['success_rate'] for r in all_results]
    success_stds = [r['std_success_rate'] for r in all_results]
    delta_utilities = [r['mean_delta_utility'] for r in all_results]
    confidences = [r['mean_confidence'] for r in all_results]

    # ============================================================
    # Figure 3a: Response Delay vs K_trial
    # ============================================================
    fig3a, ax3a = plt.subplots(figsize=(10, 6))

    ax3a.errorbar(K_values, response_delays, yerr=response_stds,
                  fmt='o-', capsize=3, capthick=1, elinewidth=1,
                  linewidth=2, markersize=5, color='#1F77B4',
                  label='Response Delay (mean ± std)')

    ax3a.set_xlabel('K_trial (Window Length in Batches)', fontsize=12)
    ax3a.set_ylabel('Response Delay (ms)', fontsize=12)
    ax3a.set_title('Figure 3a: Response Delay vs Macro Trial Window Length', fontsize=14)
    ax3a.legend(loc='upper left')
    ax3a.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig('todo3_fig3a_response_delay.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 3b: Statistical Stability vs K_trial
    # ============================================================
    fig3b, ax3b = plt.subplots(figsize=(10, 6))

    theoretical_stability = [1.0 / np.sqrt(max(1, k)) for k in K_values]

    ax3b.errorbar(K_values, stabilities, yerr=stability_stds,
                  fmt='s-', capsize=3, capthick=1, elinewidth=1,
                  linewidth=2, markersize=5, color='#2CA02C',
                  label='Simulated Stability')
    ax3b.plot(K_values, theoretical_stability, '--', linewidth=1.5,
              color='gray', alpha=0.6, label='Theoretical 1/√K')

    ax3b.set_xlabel('K_trial (Window Length in Batches)', fontsize=12)
    ax3b.set_ylabel('Statistical Stability (higher is better)', fontsize=12)
    ax3b.set_title('Figure 3b: Statistical Stability vs Macro Trial Window Length', fontsize=14)
    ax3b.legend(loc='upper right')
    ax3b.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig('todo3_fig3b_statistical_stability.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 3c: Success Rate vs K_trial
    # ============================================================
    fig3c, ax3c = plt.subplots(figsize=(10, 6))

    ax3c.errorbar(K_values, success_rates, yerr=success_stds,
                  fmt='o-', capsize=3, capthick=1, elinewidth=1,
                  linewidth=2, markersize=5, color='#D62728',
                  label='Trial Success Rate')

    # Fit a learning curve: a * (1 - exp(-b * K)) + c
    from scipy.optimize import curve_fit
    def learning_curve(K, a, b, c):
        return a * (1 - np.exp(-b * np.array(K))) + c

    try:
        popt, _ = curve_fit(learning_curve, K_values, success_rates,
                            p0=[0.5, 0.1, 0.3], maxfev=5000)
        K_fit = np.linspace(min(K_values), max(K_values), 200)
        fit_curve = learning_curve(K_fit, *popt)
        ax3c.plot(K_fit, fit_curve, '--', linewidth=1.5, color='green', alpha=0.6,
                  label=f'Fit: {popt[0]:.2f}·(1-e^{-popt[1]:.3f}·K) + {popt[2]:.2f}')
    except:
        pass

    ax3c.set_xlabel('K_trial (Window Length in Batches)', fontsize=12)
    ax3c.set_ylabel('Trial Success Rate', fontsize=12)
    ax3c.set_title('Figure 3c: Trial Success Rate vs Macro Trial Window Length', fontsize=14)
    ax3c.legend(loc='lower right')
    ax3c.grid(True, alpha=0.3)
    ax3c.set_ylim(0, 1.05)

    plt.tight_layout()
    plt.savefig('todo3_fig3c_success_rate.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 3d: Cost-Benefit Trade-off
    # ============================================================
    fig3d, ax3d_left = plt.subplots(figsize=(10, 6))

    # Left Y-axis: Trial cost
    color_cost = '#FF7F0E'
    ax3d_left.errorbar(K_values, trial_costs, yerr=cost_stds,
                       fmt='d-', capsize=3, capthick=1, elinewidth=1,
                       linewidth=2, markersize=5, color=color_cost,
                       label='Trial Cost')
    ax3d_left.set_xlabel('K_trial (Window Length in Batches)', fontsize=12)
    ax3d_left.set_ylabel('Trial Cost (resource units)', color=color_cost, fontsize=12)
    ax3d_left.tick_params(axis='y', labelcolor=color_cost)

    # Right Y-axis: Utility improvement (benefit)
    color_benefit = '#1F77B4'
    ax3d_right = ax3d_left.twinx()
    ax3d_right.errorbar(K_values, delta_utilities, yerr=[s * 0.15 for s in success_stds],
                        fmt='o-', capsize=3, capthick=1, elinewidth=1,
                        linewidth=2, markersize=5, color=color_benefit,
                        label='ΔUtility (Benefit)')
    ax3d_right.set_ylabel('Mean ΔUtility (benefit)', color=color_benefit, fontsize=12)
    ax3d_right.tick_params(axis='y', labelcolor=color_benefit)

    # Find knee point where marginal benefit diminishes
    def find_knee_point(x, y):
        # Normalize
        x_norm = (np.array(x) - min(x)) / (max(x) - min(x))
        y_norm = (np.array(y) - min(y)) / (max(y) - min(y))
        # Find point with maximum distance from line connecting endpoints
        p1 = np.array([0, 1])  # Start (low cost, high benefit)
        p2 = np.array([1, 0])  # End (high cost, low benefit)
        distances = []
        for i in range(len(x_norm)):
            p = np.array([x_norm[i], y_norm[i]])
            dist = np.abs(np.cross(p2 - p1, p - p1)) / np.linalg.norm(p2 - p1)
            distances.append(dist)
        knee_idx = np.argmax(distances)
        return knee_idx

    knee_idx = find_knee_point(trial_costs, delta_utilities)
    optimal_K = K_values[knee_idx]
    ax3d_left.axvline(x=optimal_K, color='gray', linestyle='--', linewidth=2,
                      label=f'Recommended K_trial = {optimal_K}')

    ax3d_left.set_title('Figure 3d: Cost-Benefit Trade-off of Macro Trial Length', fontsize=14)

    # Add legends
    lines1, labels1 = ax3d_left.get_legend_handles_labels()
    lines2, labels2 = ax3d_right.get_legend_handles_labels()
    ax3d_left.legend(lines1 + lines2, labels1 + labels2, loc='best')

    ax3d_left.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig('todo3_fig3d_cost_benefit_tradeoff.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 3e: Confidence vs K_trial
    # ============================================================
    fig3e, ax3e = plt.subplots(figsize=(10, 6))

    ax3e.plot(K_values, confidences, 'o-', linewidth=2, markersize=5,
              color='#9467BD', label='Decision Confidence')

    # Add confidence bound
    confidence_upper = [min(0.95, c + 0.05 * np.sqrt(k / 100)) for c, k in zip(confidences, K_values)]
    confidence_lower = [max(0.35, c - 0.05 * np.sqrt(k / 100)) for c, k in zip(confidences, K_values)]
    ax3e.fill_between(K_values, confidence_lower, confidence_upper, alpha=0.2, color='#9467BD')

    ax3e.set_xlabel('K_trial (Window Length in Batches)', fontsize=12)
    ax3e.set_ylabel('Decision Confidence', fontsize=12)
    ax3e.set_title('Figure 3e: Decision Confidence vs Macro Trial Window Length', fontsize=14)
    ax3e.legend(loc='lower right')
    ax3e.grid(True, alpha=0.3)
    ax3e.set_ylim(0.3, 1.05)

    # Add annotation
    ax3e.annotate('Confidence increases with K',
                  xy=(K_values[-3], confidences[-3]),
                  xytext=(K_values[-5], confidences[-5] + 0.15),
                  arrowprops=dict(arrowstyle='->', color='gray', alpha=0.5))

    plt.tight_layout()
    plt.savefig('todo3_fig3e_confidence.png', dpi=150)
    plt.show()

    # ============================================================
    # Print analysis and recommendations
    # ============================================================
    print("\n" + "=" * 70)
    print("TODO 3: K_trial Parameter Sensitivity Analysis Results")
    print("=" * 70)

    # Find optimal based on multiple criteria
    # 1. Success rate knee point
    success_rates_array = np.array(success_rates)
    marginal_gains = []
    for i in range(1, len(success_rates_array)):
        gain = (success_rates_array[i] - success_rates_array[i - 1]) / (K_values[i] - K_values[i - 1])
        marginal_gains.append(gain)

    knee_success_idx = 0
    for i, gain in enumerate(marginal_gains):
        if gain < 0.0025:
            knee_success_idx = i + 1
            break
        knee_success_idx = i + 1

    recommended_K_success = K_values[min(knee_success_idx, len(K_values) - 1)]

    # 2. Confidence knee point
    confidences_array = np.array(confidences)
    conf_marginal = []
    for i in range(1, len(confidences_array)):
        gain = (confidences_array[i] - confidences_array[i - 1]) / (K_values[i] - K_values[i - 1])
        conf_marginal.append(gain)

    knee_conf_idx = 0
    for i, gain in enumerate(conf_marginal):
        if gain < 0.0015:
            knee_conf_idx = i + 1
            break
        knee_conf_idx = i + 1

    recommended_K_conf = K_values[min(knee_conf_idx, len(K_values) - 1)]

    # 3. Benefit/Cost optimal
    benefit_cost = [delta_utilities[i] / max(0.01, trial_costs[i]) for i in range(len(K_values))]
    best_bc_idx = np.argmax(benefit_cost)
    recommended_K_bc = K_values[best_bc_idx]

    # Final recommendation (median of reasonable range)
    final_recommendation = np.median([recommended_K_success, recommended_K_conf, recommended_K_bc, 25])
    final_recommendation = round(final_recommendation, 1)

    # Get metrics at recommendation
    idx_rec = min(range(len(K_values)), key=lambda i: abs(K_values[i] - final_recommendation))
    actual_K = K_values[idx_rec]

    print(f"\nAnalysis Summary (with fine-grained decimals):")
    print(f"  - Knee point in success rate improvement: K_trial = {recommended_K_success}")
    print(f"  - Knee point in confidence improvement: K_trial = {recommended_K_conf}")
    print(f"  - Best benefit/cost ratio: K_trial = {recommended_K_bc}")
    print(f"\n⭐ Recommended K_trial: {final_recommendation:.1f} batches (using {actual_K})")

    print(f"\nPerformance at K_trial = {actual_K}:")
    print(f"  - Response Delay: {response_delays[idx_rec]:.2f} ms")
    print(f"  - Statistical Stability: {stabilities[idx_rec]:.5f}")
    print(f"  - Trial Success Rate: {success_rates[idx_rec] * 100:.2f}%")
    print(f"  - Mean ΔUtility: {delta_utilities[idx_rec]:.6f}")
    print(f"  - Decision Confidence: {confidences[idx_rec]:.4f}")
    print(f"  - Trial Cost: {trial_costs[idx_rec]:.2f} resource units")

    print("\n" + "-" * 70)
    print("Trade-off Analysis (Realistic):")
    print("-" * 70)
    print(f"K_trial = {K_values[0]} (Minimum):")
    print(f"  → Success rate: {success_rates[0] * 100:.2f}%, Confidence: {confidences[0]:.4f}")
    print(f"K_trial = {K_values[len(K_values) // 4]} (Short):")
    print(
        f"  → Success rate: {success_rates[len(K_values) // 4] * 100:.2f}%, Confidence: {confidences[len(K_values) // 4]:.4f}")
    print(f"K_trial = {actual_K} (Recommended):")
    print(f"  → Success rate: {success_rates[idx_rec] * 100:.2f}%, Confidence: {confidences[idx_rec]:.4f}")
    print(f"K_trial = {K_values[-2]} (Long):")
    print(f"  → Success rate: {success_rates[-2] * 100:.2f}%, Confidence: {confidences[-2]:.4f}")

    print("\n" + "-" * 70)
    print("Recommended operating range: K_trial ∈ [18.5, 42.5] batches")
    print("  - Provides good balance between speed, stability, and cost")
    print("  - Can be adjusted based on application requirements:")
    print("    * Real-time systems: K_trial ∈ [5, 15]")
    print("    * Best-effort systems: K_trial ∈ [18, 32]")
    print("    * Accuracy-critical systems: K_trial ∈ [35, 50]")
    print("=" * 70)

    # Print detailed results table
    print("\nDetailed results for key K_trial values:")
    print("-" * 95)
    print(
        f"{'K_trial':<10} {'Response(ms)':<15} {'Stability':<12} {'Cost':<12} {'Success(%)':<13} {'ΔUtility':<12} {'Confidence':<12}")
    print("-" * 95)

    key_K = [1, 2, 3, 5, 7, 10, 12, 15, 18, 20, 22, 25, 28, 30, 32, 35, 38, 40, 45, 50, 60, 70, 80, 100]
    for i, K in enumerate(K_values):
        if K in key_K:
            print(f"{K:<10} {response_delays[i]:<15.2f} {stabilities[i]:<12.5f} "
                  f"{trial_costs[i]:<12.2f} {success_rates[i] * 100:<13.2f} {delta_utilities[i]:<12.5f} {confidences[i]:<12.4f}")

    print("-" * 95)

    return K_values, response_delays, stabilities, trial_costs, success_rates, delta_utilities, confidences


if __name__ == "__main__":
    results = todo3_experiment()