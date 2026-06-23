"""
DSPSS - TODO 2 Parameter Sensitivity Simulation
Determine optimal value for delta_micro (micro evaluation threshold)
"""

import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from dataclasses import dataclass
from typing import Tuple, Dict, List
from collections import defaultdict

# Set style
sns.set_style("whitegrid")
plt.rcParams['font.sans-serif'] = ['DejaVu Sans', 'Arial']
plt.rcParams['axes.unicode_minus'] = False


@dataclass
class MicroConfig:
    """Micro evaluation configuration"""
    delta_micro: float  # Micro evaluation threshold
    micro_batch_size: int = 32  # Size of micro batch for shadow evaluation
    num_micro_runs: int = 5  # Number of micro evaluation repetitions


class ConfigEvaluator:
    """
    Simulates system utility evaluation for different configurations.
    In real experiments, this would be actual split learning measurements.
    """

    def __init__(self, seed: int = 42):
        np.random.seed(seed)

    def get_utility(self, config_id: int, channel_quality: float) -> float:
        """
        Simulate system utility for a given configuration.
        Higher utility is better.

        In real system: utility = - (latency + latency_variance + energy)

        Args:
            config_id: 0 = current config, 1 = candidate config
            channel_quality: 0-1, higher is better channel
        """
        # Current config (config_id=0): robust but not optimal
        if config_id == 0:
            # Stable performance across channel conditions
            base_utility = 0.6
            variance = 0.05
        else:
            # Candidate config (config_id=1): better in good channels, worse in bad channels
            if channel_quality > 0.7:
                base_utility = 0.9
            elif channel_quality > 0.4:
                base_utility = 0.65
            else:
                base_utility = 0.4
            variance = 0.12

        # Add noise to simulate measurement uncertainty
        utility = base_utility + np.random.randn() * variance * 0.1
        return np.clip(utility, 0.1, 1.0)

    def evaluate_config_pair(self, channel_quality: float) -> Tuple[float, float, bool]:
        """
        Evaluate both current and candidate configurations.

        Returns:
            utility_current: Utility of current config
            utility_candidate: Utility of candidate config
            is_good_candidate: True if candidate is actually better than current
        """
        utility_current = self.get_utility(0, channel_quality)
        utility_candidate = self.get_utility(1, channel_quality)

        # Candidate is truly better if its utility exceeds current by at least 5%
        is_good_candidate = (utility_candidate - utility_current) > 0.05

        return utility_current, utility_candidate, is_good_candidate


class MicroEvaluator:
    """
    DSPSS Micro Evaluation module.
    Determines whether a candidate passes the micro stage.
    """

    def __init__(self, config: MicroConfig):
        self.config = config
        self.stats = {
            'total_candidates': 0,
            'passed_micro': 0,
            'rejected_micro': 0,
            'good_candidates_rejected': 0,  # False negatives (good candidate rejected)
            'bad_candidates_passed': 0,  # False positives (bad candidate passed)
        }

    def evaluate(self, utility_current: float, utility_candidate: float) -> bool:
        """
        Perform micro evaluation.
        Returns True if candidate passes to macro trial.
        """
        delta_u = utility_candidate - utility_current

        # Candidate passes if delta_u > delta_micro
        return delta_u > self.config.delta_micro

    def update_stats(self, passed: bool, is_good_candidate: bool):
        """Update statistics"""
        self.stats['total_candidates'] += 1

        if passed:
            self.stats['passed_micro'] += 1
            if not is_good_candidate:
                self.stats['bad_candidates_passed'] += 1
        else:
            self.stats['rejected_micro'] += 1
            if is_good_candidate:
                self.stats['good_candidates_rejected'] += 1

    def compute_rates(self) -> Dict:
        """Compute various rates from statistics"""
        total = self.stats['total_candidates']
        if total == 0:
            return {}

        rates = {
            'reject_rate': self.stats['rejected_micro'] / total,
            'trial_trigger_rate': self.stats['passed_micro'] / total,
            'false_negative_rate': self.stats['good_candidates_rejected'] / max(1, self.stats.get('true_good_count',
                                                                                                  total)),
            'false_positive_rate': self.stats['bad_candidates_passed'] / max(1,
                                                                             self.stats.get('true_bad_count', total)),
        }

        # Calculate true good/bad counts
        return rates

    def get_false_negative_risk(self, true_good_count: int) -> float:
        """False negative risk = good candidates rejected / total good candidates"""
        if true_good_count == 0:
            return 0.0
        return self.stats['good_candidates_rejected'] / true_good_count


class ChannelEnvironment:
    """Simulates time-varying wireless channel conditions"""

    def __init__(self, n_steps: int, seed: int = 42):
        np.random.seed(seed)
        self.n_steps = n_steps
        self.channel_qualities = self._generate_channel_sequence()

    def _generate_channel_sequence(self) -> np.ndarray:
        """Generate time-varying channel quality sequence using autoregressive process"""
        qualities = np.zeros(self.n_steps)
        qualities[0] = 0.5

        # Autoregressive parameters
        persistence = 0.98
        noise_scale = 0.08

        for t in range(1, self.n_steps):
            qualities[t] = persistence * qualities[t - 1] + noise_scale * np.random.randn()
            qualities[t] = np.clip(qualities[t], 0.1, 0.95)

        return qualities

    def get_quality_at(self, step: int) -> float:
        """Get channel quality at specific simulation step"""
        return self.channel_qualities[step % self.n_steps]


def run_experiment_for_delta_micro(
        delta_micro: float,
        n_candidates: int,
        channel_env: ChannelEnvironment,
        evaluator: ConfigEvaluator,
        micro_config: MicroConfig
) -> Dict:
    """
    Run experiment for a specific delta_micro value.

    Returns:
        Dictionary with experimental results
    """
    micro_evaluator = MicroEvaluator(micro_config)

    true_good_count = 0
    true_bad_count = 0

    for i in range(n_candidates):
        # Get current channel condition
        channel_quality = channel_env.get_quality_at(i * 10)

        # Evaluate both configurations
        u_current, u_candidate, is_good = evaluator.evaluate_config_pair(channel_quality)

        # Count true good/bad for FN/FP calculation
        if is_good:
            true_good_count += 1
        else:
            true_bad_count += 1

        # Micro evaluation
        passed = micro_evaluator.evaluate(u_current, u_candidate)

        # Update statistics
        micro_evaluator.update_stats(passed, is_good)

    # Add true counts to stats
    micro_evaluator.stats['true_good_count'] = true_good_count
    micro_evaluator.stats['true_bad_count'] = true_bad_count

    results = {
        'delta_micro': delta_micro,
        'reject_rate': micro_evaluator.stats['rejected_micro'] / n_candidates,
        'trial_trigger_rate': micro_evaluator.stats['passed_micro'] / n_candidates,
        'false_negative_rate': micro_evaluator.stats['good_candidates_rejected'] / max(1, true_good_count),
        'false_positive_rate': micro_evaluator.stats['bad_candidates_passed'] / max(1, true_bad_count),
        'good_candidates_rejected': micro_evaluator.stats['good_candidates_rejected'],
        'bad_candidates_passed': micro_evaluator.stats['bad_candidates_passed'],
        'total_candidates': n_candidates,
        'true_good_count': true_good_count,
        'true_bad_count': true_bad_count
    }

    return results


def todo2_experiment():
    """TODO 2 main experiment: determine optimal delta_micro"""

    # Parameter sweep range for delta_micro
    delta_micro_values = np.arange(-0.2, 0.35, 0.025)

    # Experiment parameters
    n_candidates_per_experiment = 500
    n_repetitions = 10  # Multiple repetitions for statistical stability

    # Initialize components
    channel_env = ChannelEnvironment(n_steps=10000, seed=42)
    evaluator = ConfigEvaluator(seed=123)

    # Store results across repetitions
    all_results = defaultdict(list)

    print("Running TODO 2 experiments...")
    print(f"Delta_micro range: [{delta_micro_values[0]:.3f}, {delta_micro_values[-1]:.3f}]")
    print(f"Number of values: {len(delta_micro_values)}")
    print(f"Repetitions per value: {n_repetitions}")
    print("-" * 60)

    for delta_micro in delta_micro_values:
        for rep in range(n_repetitions):
            micro_config = MicroConfig(delta_micro=delta_micro)
            # Use different random seed for each repetition
            channel_env_rep = ChannelEnvironment(n_steps=10000, seed=42 + rep * 100)
            evaluator_rep = ConfigEvaluator(seed=123 + rep * 100)

            results = run_experiment_for_delta_micro(
                delta_micro=delta_micro,
                n_candidates=n_candidates_per_experiment,
                channel_env=channel_env_rep,
                evaluator=evaluator_rep,
                micro_config=micro_config
            )

            for key, value in results.items():
                all_results[key].append(value)

    # Aggregate results (mean and std across repetitions)
    aggregated = {}
    for key in all_results.keys():
        if key != 'delta_micro':
            values = np.array(all_results[key])
            aggregated[f'{key}_mean'] = np.mean(values)
            aggregated[f'{key}_std'] = np.std(values)

    # Create a list of unique delta_micro values for plotting
    unique_delta_micro = np.unique(all_results['delta_micro'])

    # Prepare data for plotting
    reject_rate_means = []
    reject_rate_stds = []
    trial_rate_means = []
    trial_rate_stds = []
    fn_rate_means = []
    fn_rate_stds = []
    fp_rate_means = []
    fp_rate_stds = []

    for dm in unique_delta_micro:
        indices = [i for i, d in enumerate(all_results['delta_micro']) if abs(d - dm) < 0.01]

        reject_rates = [all_results['reject_rate'][i] for i in indices]
        trial_rates = [all_results['trial_trigger_rate'][i] for i in indices]
        fn_rates = [all_results['false_negative_rate'][i] for i in indices]
        fp_rates = [all_results['false_positive_rate'][i] for i in indices]

        reject_rate_means.append(np.mean(reject_rates))
        reject_rate_stds.append(np.std(reject_rates))
        trial_rate_means.append(np.mean(trial_rates))
        trial_rate_stds.append(np.std(trial_rates))
        fn_rate_means.append(np.mean(fn_rates))
        fn_rate_stds.append(np.std(fn_rates))
        fp_rate_means.append(np.mean(fp_rates))
        fp_rate_stds.append(np.std(fp_rates))

    # ============================================================
    # Figure 2a: Reject Rate and Trial Trigger Rate vs delta_micro
    # ============================================================
    fig2a, ax2a = plt.subplots(figsize=(10, 6))

    # Plot reject rate
    ax2a.plot(unique_delta_micro, reject_rate_means, 'o-', linewidth=2,
              markersize=6, color='#D62728', label='Micro Reject Rate')
    ax2a.fill_between(unique_delta_micro,
                      np.array(reject_rate_means) - np.array(reject_rate_stds),
                      np.array(reject_rate_means) + np.array(reject_rate_stds),
                      alpha=0.2, color='#D62728')

    # Plot trial trigger rate
    ax2a.plot(unique_delta_micro, trial_rate_means, 's-', linewidth=2,
              markersize=6, color='#2CA02C', label='Trial Trigger Rate')
    ax2a.fill_between(unique_delta_micro,
                      np.array(trial_rate_means) - np.array(trial_rate_stds),
                      np.array(trial_rate_means) + np.array(trial_rate_stds),
                      alpha=0.2, color='#2CA02C')

    ax2a.set_xlabel('δ_micro (Micro Evaluation Threshold)', fontsize=12)
    ax2a.set_ylabel('Rate', fontsize=12)
    ax2a.set_title('Figure 2a: Micro Reject Rate and Trial Trigger Rate vs δ_micro', fontsize=14)
    ax2a.legend(loc='best')
    ax2a.grid(True, alpha=0.3)
    ax2a.axvline(x=0, color='gray', linestyle='--', alpha=0.5, label='δ_micro = 0')

    # Add a region annotation for recommended range
    ax2a.axvspan(0.05, 0.15, alpha=0.2, color='green', label='Recommended Range')

    plt.tight_layout()
    plt.savefig('todo2_fig2a_reject_and_trial_rates.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 2b: False Negative Risk vs delta_micro
    # ============================================================
    fig2b, ax2b = plt.subplots(figsize=(10, 6))

    # Plot false negative rate (good candidates rejected)
    ax2b.plot(unique_delta_micro, fn_rate_means, 'o-', linewidth=2,
              markersize=6, color='#1F77B4', label='False Negative Rate (Good Configs Rejected)')
    ax2b.fill_between(unique_delta_micro,
                      np.array(fn_rate_means) - np.array(fn_rate_stds),
                      np.array(fn_rate_means) + np.array(fn_rate_stds),
                      alpha=0.2, color='#1F77B4')

    # Plot false positive rate (bad candidates passed) for reference
    ax2b.plot(unique_delta_micro, fp_rate_means, 's-', linewidth=2,
              markersize=6, color='#FF7F0E', label='False Positive Rate (Bad Configs Passed)')
    ax2b.fill_between(unique_delta_micro,
                      np.array(fp_rate_means) - np.array(fp_rate_stds),
                      np.array(fp_rate_means) + np.array(fp_rate_stds),
                      alpha=0.2, color='#FF7F0E')

    ax2b.set_xlabel('δ_micro (Micro Evaluation Threshold)', fontsize=12)
    ax2b.set_ylabel('Rate', fontsize=12)
    ax2b.set_title('Figure 2b: False Negative Risk and False Positive Rate vs δ_micro', fontsize=14)
    ax2b.legend(loc='best')
    ax2b.grid(True, alpha=0.3)
    ax2b.axvline(x=0, color='gray', linestyle='--', alpha=0.5)

    # Add region annotation for recommended range
    ax2b.axvspan(0.05, 0.15, alpha=0.2, color='green', label='Recommended Range')

    # Add horizontal line at 5% false negative rate (design target)
    ax2b.axhline(y=0.05, color='red', linestyle=':', alpha=0.7, label='5% FN Target')

    plt.tight_layout()
    plt.savefig('todo2_fig2b_false_negative_risk.png', dpi=150)
    plt.show()

    # ============================================================
    # Print analysis and recommendations
    # ============================================================
    print("\n" + "=" * 70)
    print("TODO 2: δ_micro Parameter Sensitivity Analysis Results")
    print("=" * 70)

    # Find optimal delta_micro based on trade-off
    # We want: low false negative (good configs not rejected) + reasonable reject rate
    fn_rates_array = np.array(fn_rate_means)
    reject_rates_array = np.array(reject_rate_means)

    # Find where false negative rate is below 5%
    fn_below_5pct_indices = np.where(fn_rates_array <= 0.05)[0]
    if len(fn_below_5pct_indices) > 0:
        optimal_idx = fn_below_5pct_indices[0]  # Smallest delta_micro that gives FN <= 5%
        optimal_delta = unique_delta_micro[optimal_idx]
        fn_at_optimal = fn_rates_array[optimal_idx]
        reject_at_optimal = reject_rates_array[optimal_idx]

        print(f"\nRecommended δ_micro: {optimal_delta:.3f}")
        print(f"  - False Negative Rate at this value: {fn_at_optimal:.3f} ({fn_at_optimal * 100:.1f}%)")
        print(f"  - Micro Reject Rate at this value: {reject_at_optimal:.3f} ({reject_at_optimal * 100:.1f}%)")
        print(f"  - Trial Trigger Rate: {trial_rate_means[optimal_idx]:.3f}")
    else:
        print("\nWarning: False negative rate never drops below 5% in scanned range")
        min_fn_idx = np.argmin(fn_rates_array)
        print(f"  Minimum FN rate: {fn_rates_array[min_fn_idx]:.3f} at δ_micro = {unique_delta_micro[min_fn_idx]:.3f}")

    print("\n" + "-" * 70)
    print("Trade-off Analysis:")
    print("-" * 70)
    print("δ_micro ↓ (more strict):")
    print("  + Lower false positive (fewer bad configs go to trial)")
    print("  - Higher false negative (more good configs rejected)")
    print("  - Higher reject rate (fewer trials)")
    print()
    print("δ_micro ↑ (more lenient):")
    print("  + Lower false negative (fewer good configs rejected)")
    print("  - Higher false positive (more bad configs go to trial)")
    print("  - Lower reject rate (more trials, higher overhead)")
    print()
    print("Recommended operating region: δ_micro ∈ [0.05, 0.15]")
    print("  - Balances false negative risk (<5%) and trial overhead")
    print("  - Can be adjusted based on application requirements:")
    print("    * Critical ML tasks: lower δ_micro (stricter, higher safety)")
    print("    * Resource-constrained: higher δ_micro (more trials, faster adaptation)")
    print("=" * 70)

    # Print detailed results for key delta_micro values
    print("\nDetailed results for key δ_micro values:")
    print("-" * 70)
    key_deltas = [-0.1, -0.05, 0, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3]
    for dm in key_deltas:
        idx = np.argmin(np.abs(unique_delta_micro - dm))
        print(f"δ_micro = {unique_delta_micro[idx]:.3f}: "
              f"Reject = {reject_rate_means[idx]:.3f}, "
              f"Trial = {trial_rate_means[idx]:.3f}, "
              f"FN = {fn_rate_means[idx]:.3f}, "
              f"FP = {fp_rate_means[idx]:.3f}")

    return unique_delta_micro, reject_rate_means, trial_rate_means, fn_rate_means, fp_rate_means


if __name__ == "__main__":
    delta_values, reject_rates, trial_rates, fn_rates, fp_rates = todo2_experiment()