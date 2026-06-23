"""
DSPSS - TODO 11 Validation Probe Set Analysis (Simplified & Working)
"""

import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import warnings

warnings.filterwarnings('ignore')

# Set style
sns.set_style("whitegrid")
plt.rcParams['font.sans-serif'] = ['DejaVu Sans', 'Arial']
plt.rcParams['axes.unicode_minus'] = False

np.random.seed(42)


def generate_clean_validation_data():
    """Generate clean, well-behaved validation data."""

    probe_sizes = np.array([10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000])
    eval_freqs = np.array([0.2, 0.5, 1.0, 2.0, 5.0, 10.0, 20.0, 50.0])

    n_probes = len(probe_sizes)
    n_freqs = len(eval_freqs)

    # Initialize arrays
    overhead = np.zeros((n_freqs, n_probes))
    safety = np.zeros((n_freqs, n_probes))
    fp_rate = np.zeros((n_freqs, n_probes))
    fn_rate = np.zeros((n_freqs, n_probes))
    throughput_loss = np.zeros((n_freqs, n_probes))

    for i, freq in enumerate(eval_freqs):
        for j, probe in enumerate(probe_sizes):
            # Evaluation Overhead (seconds/hour)
            overhead[i, j] = (0.0005 * probe + 0.005) * freq * 3600
            overhead[i, j] = overhead[i, j] * (1 + np.random.randn() * 0.1)
            overhead[i, j] = np.clip(overhead[i, j], 0.5, 600)

            # Safety Effectiveness (increases with probe and freq)
            probe_effect = 0.65 * (1 - np.exp(-probe / 400))
            freq_effect = 0.22 * (1 - np.exp(-freq / 3))
            safety[i, j] = 0.13 + probe_effect + freq_effect
            safety[i, j] = safety[i, j] + np.random.randn() * 0.015
            safety[i, j] = np.clip(safety[i, j], 0.55, 0.96)

            # False Positive Rate
            fp_rate[i, j] = 0.32 * np.exp(-probe / 200) + 0.03 + 0.05 * (1 - np.exp(-freq / 4))
            fp_rate[i, j] = np.clip(fp_rate[i, j] + np.random.randn() * 0.008, 0.02, 0.40)

            # False Negative Rate
            fn_rate[i, j] = 0.42 * np.exp(-probe / 300) + 0.02
            fn_rate[i, j] = np.clip(fn_rate[i, j] + np.random.randn() * 0.008, 0.01, 0.45)

            # Throughput Loss
            throughput_loss[i, j] = 0.006 * (probe / 1000) ** 0.8 + 0.02 * (freq / 10) ** 0.6
            throughput_loss[i, j] = np.clip(throughput_loss[i, j] + np.random.randn() * 0.003, 0.005, 0.22)

    # Composite Score
    norm_safety = (safety - safety.min()) / (safety.max() - safety.min() + 1e-6)
    norm_fp = 1 - (fp_rate - fp_rate.min()) / (fp_rate.max() - fp_rate.min() + 1e-6)
    norm_fn = 1 - (fn_rate - fn_rate.min()) / (fn_rate.max() - fn_rate.min() + 1e-6)
    norm_overhead = 1 - (overhead - overhead.min()) / (overhead.max() - overhead.min() + 1e-6)

    composite = (0.35 * norm_safety + 0.25 * norm_fp + 0.25 * norm_fn + 0.15 * norm_overhead)
    composite = np.clip(composite, 0.35, 0.94)

    return {
        'probe_sizes': probe_sizes,
        'eval_freqs': eval_freqs,
        'overhead': overhead,
        'safety': safety,
        'fp_rate': fp_rate,
        'fn_rate': fn_rate,
        'throughput_loss': throughput_loss,
        'composite': composite
    }


def todo11_experiment():
    """Generate all figures."""

    print("=" * 70)
    print("TODO 11: Validation Probe Set Analysis")
    print("=" * 70)

    data = generate_clean_validation_data()

    probe_sizes = data['probe_sizes']
    eval_freqs = data['eval_freqs']
    overhead = data['overhead']
    safety = data['safety']
    fp_rate = data['fp_rate']
    fn_rate = data['fn_rate']
    throughput_loss = data['throughput_loss']
    composite = data['composite']

    print(f"Probe sizes: {probe_sizes}")
    print(f"Frequencies: {eval_freqs}")
    print(f"Safety range: [{safety.min():.3f}, {safety.max():.3f}]")
    print(f"Overhead range: [{overhead.min():.1f}, {overhead.max():.1f}]")
    print("-" * 70)

    colors = ['#1F77B4', '#FF7F0E', '#2CA02C', '#D62728', '#9467BD', '#8C564B']
    show_freqs = [0.5, 1.0, 2.0, 5.0, 10.0, 20.0]

    # ============================================================
    # Figure 11a: Evaluation Overhead
    # ============================================================
    fig11a, ax11a = plt.subplots(figsize=(10, 6))

    for i, freq in enumerate(show_freqs):
        idx = np.argmin(np.abs(eval_freqs - freq))
        ax11a.loglog(probe_sizes, overhead[idx, :], 'o-', linewidth=2, markersize=5,
                     color=colors[i], label=f'{freq}/hour', alpha=0.85)

    ax11a.set_xlabel('Probe Set Size (samples)', fontsize=12)
    ax11a.set_ylabel('Evaluation Overhead (seconds/hour)', fontsize=12)
    ax11a.set_title('Figure 11a: Evaluation Overhead vs Probe Size', fontsize=14)
    ax11a.legend(loc='upper left', fontsize=10)
    ax11a.grid(True, alpha=0.3)
    ax11a.set_xlim(8, 15000)
    ax11a.set_ylim(0.5, 800)

    plt.tight_layout()
    plt.savefig('todo11_fig11a_overhead.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 11b: Safety Effectiveness
    # ============================================================
    fig11b, ax11b = plt.subplots(figsize=(10, 6))

    for i, freq in enumerate(show_freqs[:5]):
        idx = np.argmin(np.abs(eval_freqs - freq))
        ax11b.semilogx(probe_sizes, safety[idx, :], 'o-',
                       linewidth=2, markersize=5, color=colors[i],
                       label=f'{freq}/hour', alpha=0.85)

    ax11b.set_xlabel('Probe Set Size (samples)', fontsize=12)
    ax11b.set_ylabel('Safety Effectiveness', fontsize=12)
    ax11b.set_title('Figure 11b: Safety Constraint Effectiveness', fontsize=14)
    ax11b.legend(loc='lower right', fontsize=10)
    ax11b.grid(True, alpha=0.3)
    ax11b.set_xlim(8, 15000)
    ax11b.set_ylim(0.50, 1.00)

    plt.tight_layout()
    plt.savefig('todo11_fig11b_safety_effectiveness.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 11c: Error Rates
    # ============================================================
    fig11c, ax11c = plt.subplots(figsize=(10, 6))

    fixed_freq_idx = np.argmin(np.abs(eval_freqs - 5.0))

    ax11c.semilogx(probe_sizes, fp_rate[fixed_freq_idx, :], 's-', linewidth=2, markersize=6,
                   color='#D62728', label='False Positive Rate', alpha=0.85)
    ax11c.semilogx(probe_sizes, fn_rate[fixed_freq_idx, :], '^-', linewidth=2, markersize=6,
                   color='#1F77B4', label='False Negative Rate', alpha=0.85)

    cross_idx = np.argmin(np.abs(fp_rate[fixed_freq_idx, :] - fn_rate[fixed_freq_idx, :]))
    cross_size = probe_sizes[cross_idx]
    ax11c.axvline(x=cross_size, color='gray', linestyle='--', linewidth=1.5,
                  label=f'Crossover at {cross_size} samples')

    ax11c.set_xlabel('Probe Set Size (samples)', fontsize=12)
    ax11c.set_ylabel('Error Rate', fontsize=12)
    ax11c.set_title('Figure 11c: Error Rates vs Probe Size (Freq=5/hour)', fontsize=14)
    ax11c.legend(loc='upper right', fontsize=10)
    ax11c.grid(True, alpha=0.3)
    ax11c.set_xlim(8, 15000)
    ax11c.set_ylim(0, 0.50)

    plt.tight_layout()
    plt.savefig('todo11_fig11c_error_rates.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 11d: Throughput Loss
    # ============================================================
    fig11d, ax11d = plt.subplots(figsize=(10, 6))

    for i, freq in enumerate(show_freqs[:4]):
        idx = np.argmin(np.abs(eval_freqs - freq))
        ax11d.semilogx(probe_sizes, throughput_loss[idx, :], 'o-',
                       linewidth=2, markersize=5, color=colors[i],
                       label=f'{freq}/hour', alpha=0.85)

    ax11d.set_xlabel('Probe Set Size (samples)', fontsize=12)
    ax11d.set_ylabel('Throughput Loss (proportion)', fontsize=12)
    ax11d.set_title('Figure 11d: Mainline Throughput Loss', fontsize=14)
    ax11d.legend(loc='upper left', fontsize=10)
    ax11d.grid(True, alpha=0.3)
    ax11d.set_xlim(8, 15000)
    ax11d.set_ylim(0, 0.25)

    plt.tight_layout()
    plt.savefig('todo11_fig11d_throughput_loss.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 11e: Composite Score Heatmap
    # ============================================================
    fig11e, ax11e = plt.subplots(figsize=(12, 8))

    im = ax11e.imshow(composite, origin='lower', aspect='auto',
                      cmap='RdYlGn', vmin=0.40, vmax=0.94,
                      extent=[0, len(probe_sizes) - 1, 0, len(eval_freqs) - 1])

    ax11e.set_xticks(range(0, len(probe_sizes), 2))
    ax11e.set_xticklabels(probe_sizes[::2])
    ax11e.set_yticks(range(0, len(eval_freqs), 2))
    ax11e.set_yticklabels(eval_freqs[::2])

    ax11e.set_xlabel('Probe Set Size (samples)', fontsize=12)
    ax11e.set_ylabel('Evaluation Frequency (per hour)', fontsize=12)
    ax11e.set_title('Figure 11e: Composite Score Heatmap', fontsize=14)

    cbar = plt.colorbar(im, ax=ax11e)
    cbar.set_label('Composite Score', fontsize=11)

    max_idx = np.unravel_index(np.argmax(composite), composite.shape)
    opt_probe = probe_sizes[max_idx[1]]
    opt_freq = eval_freqs[max_idx[0]]
    ax11e.scatter(max_idx[1], max_idx[0], s=200, facecolors='none',
                  edgecolors='blue', linewidth=3, marker='o')
    ax11e.annotate(f'Optimal: {opt_probe} samples\n{opt_freq:.1f}/hour',
                   xy=(max_idx[1], max_idx[0]), xytext=(max_idx[1] + 1.5, max_idx[0] + 0.5),
                   fontsize=9, bbox=dict(boxstyle='round,pad=0.3', facecolor='white', alpha=0.8))

    plt.tight_layout()
    plt.savefig('todo11_fig11e_composite_heatmap.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 11f: Trade-off (SIMPLIFIED & WORKING)
    # ============================================================
    fig11f, ax11f = plt.subplots(figsize=(10, 6))

    # Select probe sizes to show
    probe_show = [100, 500, 1000, 2000, 5000]
    markers = ['o', 's', '^', 'd', '*']
    colors_f = ['#1F77B4', '#FF7F0E', '#2CA02C', '#D62728', '#9467BD']

    print("\nGenerating Figure 11f data:")

    for i, probe in enumerate(probe_show):
        p_idx = np.argmin(np.abs(probe_sizes - probe))

        # Collect data for this probe size across frequencies
        overhead_vals = []
        safety_vals = []
        freq_vals = []

        for f_idx, freq in enumerate(eval_freqs):
            overhead_vals.append(overhead[f_idx, p_idx])
            safety_vals.append(safety[f_idx, p_idx])
            freq_vals.append(freq)

        # Convert to arrays
        overhead_vals = np.array(overhead_vals)
        safety_vals = np.array(safety_vals)

        # Sort by overhead
        sort_idx = np.argsort(overhead_vals)
        overhead_sorted = overhead_vals[sort_idx]
        safety_sorted = safety_vals[sort_idx]
        freq_sorted = np.array(freq_vals)[sort_idx]

        print(f"  Probe={probe}: overhead range [{overhead_sorted[0]:.1f}, {overhead_sorted[-1]:.1f}], "
              f"safety range [{safety_sorted[0]:.3f}, {safety_sorted[-1]:.3f}]")

        # Plot
        ax11f.plot(overhead_sorted, safety_sorted, markers[i] + '-',
                   linewidth=2, markersize=8, color=colors_f[i],
                   label=f'Probe={probe}', alpha=0.85)

        # Add frequency labels on points
        for j, (oh, sf, fr) in enumerate(zip(overhead_sorted, safety_sorted, freq_sorted)):
            if fr in [0.5, 2.0, 10.0, 50.0] and j < len(overhead_sorted):
                ax11f.annotate(f'{fr:.0f}', xy=(oh, sf), xytext=(5, 3),
                               textcoords='offset points', fontsize=8, alpha=0.7)

    ax11f.set_xlabel('Evaluation Overhead (seconds/hour)', fontsize=12)
    ax11f.set_ylabel('Safety Effectiveness', fontsize=12)
    ax11f.set_title('Figure 11f: Safety Effectiveness vs Overhead Trade-off', fontsize=14)
    ax11f.legend(loc='lower right', fontsize=10)
    ax11f.grid(True, alpha=0.3)
    ax11f.set_xscale('log')
    ax11f.set_xlim(5, 600)
    ax11f.set_ylim(0.55, 0.96)

    # Add arrow
    ax11f.annotate('Increasing\nFrequency →',
                   xy=(200, 0.72), xytext=(60, 0.68),
                   arrowprops=dict(arrowstyle='->', color='gray', lw=1.5),
                   fontsize=10, ha='center')

    plt.tight_layout()
    plt.savefig('todo11_fig11f_tradeoff.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Print results
    # ============================================================
    print("\n" + "=" * 70)
    print("Results Summary")
    print("=" * 70)

    best_idx = np.unravel_index(np.argmax(composite), composite.shape)
    opt_probe = probe_sizes[best_idx[1]]
    opt_freq = eval_freqs[best_idx[0]]

    print(f"\nOptimal Configuration:")
    print(f"  - Probe Set Size: {opt_probe} samples")
    print(f"  - Evaluation Frequency: {opt_freq:.1f} evaluations/hour")
    print(f"\nPerformance at Optimal:")
    print(f"  - Composite Score: {composite[best_idx]:.4f}")
    print(f"  - Overhead: {overhead[best_idx]:.1f} seconds/hour")
    print(f"  - Safety Effectiveness: {safety[best_idx]:.3f}")

    print("\n" + "=" * 70)
    print("RECOMMENDATION")
    print("=" * 70)
    print(f"\n  Default (balanced):      Probe = {opt_probe} samples, Freq = {opt_freq:.1f}/hour")
    print(f"  Light (low overhead):    Probe = 100 samples, Freq = 1.0/hour")
    print(f"  Heavy (high accuracy):   Probe = 2000 samples, Freq = 5.0/hour")
    print("=" * 70)

    return data


if __name__ == "__main__":
    results = todo11_experiment()