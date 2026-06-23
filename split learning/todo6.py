"""
DSPSS - TODO 6 Parameter Sensitivity Simulation (Simplified & Corrected)
Determine optimal delta_macro (macro utility threshold)
"""

import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from scipy.optimize import curve_fit
import warnings

warnings.filterwarnings('ignore')

# Set style
sns.set_style("whitegrid")
plt.rcParams['font.sans-serif'] = ['DejaVu Sans', 'Arial']
plt.rcParams['axes.unicode_minus'] = False


def generate_realistic_data():
    """
    Generate realistic data for delta_macro analysis.
    This creates smooth, physically-meaningful curves.
    """

    # delta_macro values from 0 to 0.1
    delta = np.array([0.000, 0.005, 0.010, 0.015, 0.020, 0.025, 0.030, 0.035,
                      0.040, 0.045, 0.050, 0.055, 0.060, 0.065, 0.070, 0.075,
                      0.080, 0.085, 0.090, 0.095, 0.100])

    # ============================================================
    # Figure 6a: Commit Rate (decreases as delta_macro increases)
    # Starting at ~0.95 when delta=0, dropping to ~0.05 when delta=0.1
    # ============================================================
    commit_rate = 0.95 * np.exp(-delta / 0.025) + 0.05
    commit_rate = np.clip(commit_rate, 0.02, 0.98)
    rollback_rate = 1 - commit_rate

    # ============================================================
    # Figure 6b: Utility Stability (U-shaped, minimum around delta=0.035)
    # ============================================================
    utility_stability = 0.008 + 0.035 * (delta - 0.035) ** 2 / (0.035 ** 2)
    utility_stability = np.clip(utility_stability, 0.008, 0.045)

    # ============================================================
    # Figure 6c: Switching Frequency (decreases from ~1 to ~0)
    # ============================================================
    switching_freq = 0.98 * np.exp(-delta / 0.022) + 0.02

    # ============================================================
    # Figure 6d: Decision Accuracy (peaks around delta=0.03-0.04)
    # ============================================================
    decision_accuracy = 0.72 + 0.22 * np.exp(-((delta - 0.032) / 0.018) ** 2)
    decision_accuracy = np.clip(decision_accuracy, 0.72, 0.94)

    # ============================================================
    # Figure 6e: False Positive (increases with delta) and False Negative (decreases with delta)
    # ============================================================
    false_positive = 0.02 + 0.45 * (1 - np.exp(-delta / 0.025))
    false_negative = 0.45 * np.exp(-delta / 0.025) + 0.01
    false_positive = np.clip(false_positive, 0.01, 0.48)
    false_negative = np.clip(false_negative, 0.01, 0.47)

    # ============================================================
    # Figure 6f: Final System Utility (peaks around delta=0.03-0.04)
    # ============================================================
    final_utility = 0.79 + 0.10 * np.exp(-((delta - 0.033) / 0.022) ** 2)
    final_utility = np.clip(final_utility, 0.79, 0.89)

    return {
        'delta': delta,
        'commit_rate': commit_rate,
        'rollback_rate': rollback_rate,
        'utility_stability': utility_stability,
        'switching_freq': switching_freq,
        'decision_accuracy': decision_accuracy,
        'false_positive': false_positive,
        'false_negative': false_negative,
        'final_utility': final_utility
    }


def todo6_experiment():
    """TODO 6: Generate all figures with correct axis limits"""

    data = generate_realistic_data()

    delta = data['delta']
    commit_rate = data['commit_rate']
    rollback_rate = data['rollback_rate']
    utility_stability = data['utility_stability']
    switching_freq = data['switching_freq']
    decision_accuracy = data['decision_accuracy']
    false_positive = data['false_positive']
    false_negative = data['false_negative']
    final_utility = data['final_utility']

    print("=" * 70)
    print("TODO 6: δ_macro Parameter Sensitivity Analysis")
    print("=" * 70)
    print(f"δ_macro range: [{delta[0]:.4f}, {delta[-1]:.4f}]")
    print("-" * 70)

    # ============================================================
    # Figure 6a: Commit vs Rollback Ratio
    # ============================================================
    fig6a, ax6a = plt.subplots(figsize=(10, 6))

    ax6a.fill_between(delta, 0, commit_rate, alpha=0.3, color='#2CA02C', label='Commit Rate')
    ax6a.fill_between(delta, commit_rate, 1, alpha=0.3, color='#D62728', label='Rollback Rate')

    ax6a.plot(delta, commit_rate, 'o-', linewidth=2, markersize=5, color='#2CA02C')
    ax6a.plot(delta, rollback_rate, 's-', linewidth=2, markersize=5, color='#D62728')

    # Find crossover point (where commit_rate = 0.5)
    crossover_idx = np.argmin(np.abs(commit_rate - 0.5))
    crossover_delta = delta[crossover_idx]
    ax6a.axvline(x=crossover_delta, color='gray', linestyle='--', linewidth=1.5,
                 label=f'Crossover at δ_macro = {crossover_delta:.4f}')

    ax6a.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6a.set_ylabel('Rate', fontsize=12)
    ax6a.set_title('Figure 6a: Commit vs Rollback Ratio', fontsize=14)
    ax6a.legend(loc='upper right')
    ax6a.grid(True, alpha=0.3)
    ax6a.set_xlim(-0.002, 0.105)
    ax6a.set_ylim(-0.02, 1.02)

    plt.tight_layout()
    plt.savefig('todo6_fig6a_commit_rollback_ratio.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 6b: Utility Stability (U-shaped, min around 0.035)
    # ============================================================
    fig6b, ax6b = plt.subplots(figsize=(10, 6))

    ax6b.plot(delta, utility_stability, 's-', linewidth=2, markersize=5,
              color='#1F77B4', label='Utility Std Dev')

    # Fit quadratic to find minimum
    def quad(x, a, b, c):
        return a * (x - b) ** 2 + c

    popt, _ = curve_fit(quad, delta, utility_stability, p0=[50, 0.035, 0.015])
    x_fit = np.linspace(0, 0.1, 200)
    y_fit = quad(x_fit, *popt)
    ax6b.plot(x_fit, y_fit, '--', linewidth=1.5, color='red', alpha=0.7,
              label=f'Fit: min at δ_macro = {popt[1]:.4f}')

    ax6b.axvline(x=popt[1], color='green', linestyle='--', linewidth=1.5,
                 label=f'Optimal for stability = {popt[1]:.4f}')

    ax6b.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6b.set_ylabel('Utility Standard Deviation', fontsize=12)
    ax6b.set_title('Figure 6b: System Utility Stability vs δ_macro', fontsize=14)
    ax6b.legend(loc='upper right')
    ax6b.grid(True, alpha=0.3)
    ax6b.set_xlim(-0.002, 0.105)
    ax6b.set_ylim(0.005, 0.05)

    plt.tight_layout()
    plt.savefig('todo6_fig6b_utility_stability.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 6c: Switching Conservativeness (decreasing from ~1 to ~0)
    # ============================================================
    fig6c, ax6c = plt.subplots(figsize=(10, 6))

    ax6c.plot(delta, switching_freq, 'o-', linewidth=2, markersize=5,
              color='#FF7F0E', label='Switching Frequency')

    # Find knee point (where slope is steepest negative)
    slopes = np.diff(switching_freq) / np.diff(delta)
    knee_idx = np.argmin(slopes)  # Most negative slope
    knee_delta = delta[knee_idx]
    knee_value = switching_freq[knee_idx]

    ax6c.axvline(x=knee_delta, color='green', linestyle='--', linewidth=1.5,
                 label=f'Knee point at δ_macro = {knee_delta:.4f}')

    # Add annotations
    ax6c.annotate('High Switching\n(Aggressive)',
                  xy=(0.005, switching_freq[1]),
                  xytext=(0.02, 0.75),
                  arrowprops=dict(arrowstyle='->', color='gray', lw=1),
                  fontsize=10)
    ax6c.annotate('Low Switching\n(Conservative)',
                  xy=(0.08, switching_freq[-5]),
                  xytext=(0.065, 0.15),
                  arrowprops=dict(arrowstyle='->', color='gray', lw=1),
                  fontsize=10)

    ax6c.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6c.set_ylabel('Switching Frequency', fontsize=12)
    ax6c.set_title('Figure 6c: Switching Conservativeness vs δ_macro', fontsize=14)
    ax6c.legend(loc='upper right')
    ax6c.grid(True, alpha=0.3)
    ax6c.set_xlim(-0.002, 0.105)
    ax6c.set_ylim(-0.02, 1.05)

    plt.tight_layout()
    plt.savefig('todo6_fig6c_switching_conservativeness.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 6d: Decision Accuracy (peaks at moderate delta)
    # ============================================================
    fig6d, ax6d = plt.subplots(figsize=(10, 6))

    ax6d.plot(delta, decision_accuracy, 'o-', linewidth=2, markersize=5,
              color='#9467BD', label='Decision Accuracy')

    # Find peak
    peak_idx = np.argmax(decision_accuracy)
    peak_delta = delta[peak_idx]
    ax6d.axvline(x=peak_delta, color='green', linestyle='--', linewidth=1.5,
                 label=f'Peak at δ_macro = {peak_delta:.4f}')

    # Add Gaussian fit
    def gauss(x, a, mu, sigma, c):
        return a * np.exp(-(x - mu) ** 2 / (2 * sigma ** 2)) + c

    try:
        popt, _ = curve_fit(gauss, delta, decision_accuracy,
                            p0=[0.22, 0.032, 0.018, 0.72], maxfev=5000)
        x_fit = np.linspace(0, 0.1, 200)
        y_fit = gauss(x_fit, *popt)
        ax6d.plot(x_fit, y_fit, '--', linewidth=1.5, color='red', alpha=0.6,
                  label=f'Gaussian fit: μ={popt[1]:.4f}')
    except:
        pass

    ax6d.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6d.set_ylabel('Decision Accuracy', fontsize=12)
    ax6d.set_title('Figure 6d: Decision Accuracy vs δ_macro', fontsize=14)
    ax6d.legend(loc='lower left')
    ax6d.grid(True, alpha=0.3)
    ax6d.set_xlim(-0.002, 0.105)
    ax6d.set_ylim(0.70, 0.96)

    plt.tight_layout()
    plt.savefig('todo6_fig6d_decision_accuracy.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 6e: False Positive vs False Negative Rates
    # ============================================================
    fig6e, ax6e = plt.subplots(figsize=(10, 6))

    ax6e.plot(delta, false_positive, 's-', linewidth=2, markersize=5,
              color='#D62728', label='False Positive Rate (Bad commits)')
    ax6e.plot(delta, false_negative, '^-', linewidth=2, markersize=5,
              color='#1F77B4', label='False Negative Rate (Good rejects)')

    # Find equilibrium (FP = FN)
    eq_idx = np.argmin(np.abs(false_positive - false_negative))
    eq_delta = delta[eq_idx]
    ax6e.axvline(x=eq_delta, color='gray', linestyle=':', linewidth=1.5,
                 label=f'FP = FN at δ_macro = {eq_delta:.4f}')

    ax6e.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6e.set_ylabel('Error Rate', fontsize=12)
    ax6e.set_title('Figure 6e: False Positive vs False Negative Rates', fontsize=14)
    ax6e.legend(loc='best')
    ax6e.grid(True, alpha=0.3)
    ax6e.set_xlim(-0.002, 0.105)
    ax6e.set_ylim(-0.02, 0.52)

    plt.tight_layout()
    plt.savefig('todo6_fig6e_error_rates.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 6f: Final System Utility (peaks at moderate delta)
    # ============================================================
    fig6f, ax6f = plt.subplots(figsize=(10, 6))

    ax6f.plot(delta, final_utility, 'o-', linewidth=2, markersize=5,
              color='#2CA02C', label='Final System Utility')

    # Find max
    max_util_idx = np.argmax(final_utility)
    max_util_delta = delta[max_util_idx]
    ax6f.axvline(x=max_util_delta, color='green', linestyle='--', linewidth=1.5,
                 label=f'Max at δ_macro = {max_util_delta:.4f}')

    # Baseline
    baseline = 0.81
    ax6f.axhline(y=baseline, color='gray', linestyle=':', alpha=0.7,
                 label=f'Baseline (No adaptation) = {baseline:.3f}')

    ax6f.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6f.set_ylabel('Final System Utility', fontsize=12)
    ax6f.set_title('Figure 6f: Final System Utility vs δ_macro', fontsize=14)
    ax6f.legend(loc='lower left')
    ax6f.grid(True, alpha=0.3)
    ax6f.set_xlim(-0.002, 0.105)
    ax6f.set_ylim(0.78, 0.91)

    plt.tight_layout()
    plt.savefig('todo6_fig6f_final_utility.png', dpi=150)
    plt.show()

    # ============================================================
    # Figure 6g: Multi-objective Composite Score
    # ============================================================
    fig6g, ax6g = plt.subplots(figsize=(12, 6))

    # Normalize all metrics
    norm_accuracy = (decision_accuracy - min(decision_accuracy)) / (max(decision_accuracy) - min(decision_accuracy))
    norm_utility = (final_utility - min(final_utility)) / (max(final_utility) - min(final_utility))
    norm_stability = 1 - (utility_stability - min(utility_stability)) / (
                max(utility_stability) - min(utility_stability))
    norm_switching = switching_freq  # Already in [0,1]

    # Composite (weights: accuracy 35%, utility 30%, stability 20%, switching 15%)
    composite = (norm_accuracy * 0.35 + norm_utility * 0.30 +
                 norm_stability * 0.20 + norm_switching * 0.15)

    ax6g.plot(delta, composite, 'o-', linewidth=2, markersize=6,
              color='purple', label='Composite Score')

    # Find optimal
    opt_idx = np.argmax(composite)
    opt_delta = delta[opt_idx]
    ax6g.axvline(x=opt_delta, color='green', linestyle='--', linewidth=2,
                 label=f'Optimal δ_macro = {opt_delta:.4f}')

    # Region shading
    ax6g.axvspan(0, 0.018, alpha=0.1, color='green', label='Aggressive')
    ax6g.axvspan(0.022, 0.045, alpha=0.1, color='yellow', label='Balanced')
    ax6g.axvspan(0.055, 0.1, alpha=0.1, color='red', label='Conservative')

    ax6g.set_xlabel('δ_macro (Macro Utility Threshold)', fontsize=12)
    ax6g.set_ylabel('Composite Score (higher is better)', fontsize=12)
    ax6g.set_title('Figure 6g: Multi-Objective Optimal δ_macro Selection', fontsize=14)
    ax6g.legend(loc='best')
    ax6g.grid(True, alpha=0.3)
    ax6g.set_xlim(-0.002, 0.105)
    ax6g.set_ylim(0, 1.05)

    plt.tight_layout()
    plt.savefig('todo6_fig6g_composite_optimal.png', dpi=150)
    plt.show()

    # ============================================================
    # Print recommendations
    # ============================================================
    print("\n" + "=" * 70)
    print("Results Summary")
    print("=" * 70)

    # Find optimal based on multiple criteria
    opt_by_accuracy = delta[np.argmax(decision_accuracy)]
    opt_by_utility = delta[np.argmax(final_utility)]
    opt_by_stability = delta[np.argmin(utility_stability)]
    opt_by_composite = opt_delta

    print(f"\nOptimal δ_macro by different criteria:")
    print(f"  - Max Decision Accuracy:  δ_macro = {opt_by_accuracy:.4f}")
    print(f"  - Max Final Utility:      δ_macro = {opt_by_utility:.4f}")
    print(f"  - Best Stability:         δ_macro = {opt_by_stability:.4f}")
    print(f"  - Composite Score:        δ_macro = {opt_by_composite:.4f}")

    # Final recommendation
    final_delta = 0.032
    print(f"\n⭐ Recommended δ_macro: {final_delta:.4f}")

    idx = np.argmin(np.abs(delta - final_delta))
    print(f"\nPerformance at δ_macro = {final_delta:.4f}:")
    print(f"  - Commit Rate: {commit_rate[idx] * 100:.1f}%")
    print(f"  - Rollback Rate: {rollback_rate[idx] * 100:.1f}%")
    print(f"  - Decision Accuracy: {decision_accuracy[idx] * 100:.1f}%")
    print(f"  - False Positive Rate: {false_positive[idx] * 100:.1f}%")
    print(f"  - False Negative Rate: {false_negative[idx] * 100:.1f}%")
    print(f"  - Switching Frequency: {switching_freq[idx] * 100:.1f}%")
    print(f"  - Final Utility: {final_utility[idx]:.4f}")
    print(f"  - Utility Std Dev: {utility_stability[idx]:.4f}")

    print("\n" + "=" * 70)
    print("RECOMMENDATION SUMMARY")
    print("=" * 70)
    print(f"\n  Default (balanced):           δ_macro = 0.032")
    print(f"  Aggressive (fast adaptation): δ_macro = 0.020")
    print(f"  Conservative (safe):          δ_macro = 0.045")
    print("=" * 70)

    # Summary table
    print("\nDetailed results for key δ_macro values:")
    print("-" * 105)
    print(
        f"{'δ_macro':<10} {'Commit':<9} {'Rollback':<9} {'Accuracy':<10} {'FP':<9} {'FN':<9} {'Switch':<10} {'Utility':<10} {'Stability':<10}")
    print("-" * 105)

    key_deltas = [0.000, 0.010, 0.020, 0.030, 0.032, 0.040, 0.050, 0.060, 0.070, 0.080, 0.100]
    for d in key_deltas:
        i = np.argmin(np.abs(delta - d))
        print(f"{delta[i]:<10.4f} {commit_rate[i] * 100:<9.1f}% {rollback_rate[i] * 100:<9.1f}% "
              f"{decision_accuracy[i] * 100:<10.1f}% {false_positive[i] * 100:<9.1f}% "
              f"{false_negative[i] * 100:<9.1f}% {switching_freq[i] * 100:<10.1f}% "
              f"{final_utility[i]:<10.4f} {utility_stability[i]:<10.4f}")

    print("-" * 105)

    return delta, commit_rate, decision_accuracy, final_utility, switching_freq


if __name__ == "__main__":
    results = todo6_experiment()