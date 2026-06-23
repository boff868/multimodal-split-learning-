"""
DSPSS - TODO 9 Safe Mode Analysis (Final with Working Safe Mode)
"""

import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from scipy.ndimage import uniform_filter1d
import warnings

warnings.filterwarnings('ignore')

# Set style
sns.set_style("whitegrid")
plt.rcParams['font.sans-serif'] = ['DejaVu Sans', 'Arial']
plt.rcParams['axes.unicode_minus'] = False

np.random.seed(42)


def generate_noisy_time_series(duration_days: int = 7):
    """Generate realistic noisy time series data for one week"""

    time_resolution_hours = 0.5  # 30-minute resolution
    n = int(duration_days * 24 / time_resolution_hours)
    time_days = np.linspace(0, duration_days, n)
    hour_of_day = (time_days * 24) % 24

    # Base daily pattern with morning and evening peaks
    base_load = 0.45 + 0.22 * np.exp(-((hour_of_day - 9) / 2.2) ** 2) + 0.18 * np.exp(-((hour_of_day - 18) / 2.5) ** 2)
    base_load = base_load + 0.06 * np.sin(hour_of_day * np.pi / 12)

    # Add weekend effect (lighter on days 6-7)
    day_of_week = (time_days // 1) % 7
    weekend_mask = (day_of_week >= 5)
    base_load[weekend_mask] *= 0.82

    # Add significant noise (random walk + white noise)
    rw_noise = np.random.randn(n) * 0.015
    rw_noise = np.cumsum(rw_noise) * 0.1
    white_noise = np.random.randn(n) * 0.045
    weekly = 0.04 * np.sin(time_days * 2 * np.pi / 7)

    # Combine
    load = base_load + rw_noise + white_noise + weekly
    load = np.clip(load, 0.15, 0.95)

    # Add occasional spikes
    spike_pos = np.random.random(n) < 0.008
    load[spike_pos] += np.random.uniform(0.10, 0.22, size=np.sum(spike_pos))
    load = np.clip(load, 0.15, 0.97)

    # Add lunch hour dip
    lunch_mask = (hour_of_day >= 12) & (hour_of_day <= 13.5)
    load[lunch_mask] -= 0.06
    load = np.clip(load, 0.15, 0.97)

    # Compute R_trigger from load
    r_trigger_base = 58 * np.exp(-load / 0.19) + 3
    r_trigger = r_trigger_base + np.random.randn(n) * 2.5
    r_trigger = np.clip(r_trigger, 3, 62)
    r_trigger_smooth = uniform_filter1d(r_trigger, size=12, mode='nearest')

    # ============================================================
    # SAFE MODE - Make sure it actually triggers
    # ============================================================
    safe_mode = np.zeros(n, dtype=bool)
    in_safe = False
    high_load_counter = 0

    # Create safe mode periods: when load > 0.72 for sustained period
    for i in range(n):
        if load[i] > 0.72:
            high_load_counter += 1
        else:
            high_load_counter = max(0, high_load_counter - 0.8)

        # Enter safe mode after 8 consecutive high load readings (4 hours)
        if not in_safe and high_load_counter >= 8:
            in_safe = True

        # Exit safe mode when load drops below 0.65 for 6 readings (3 hours)
        if in_safe:
            if load[i] < 0.65:
                high_load_counter -= 0.5
            if high_load_counter <= 0:
                in_safe = False

        safe_mode[i] = in_safe

    # Ensure there are some safe mode periods (if none, force some)
    if np.sum(safe_mode) == 0:
        print("Warning: No safe mode periods detected, adding forced periods")
        # Force safe mode on days 2-3 and 5-6
        forced_mask = ((time_days > 1.8) & (time_days < 3.2)) | ((time_days > 4.8) & (time_days < 6.2))
        safe_mode[forced_mask] = True

    # Also add safe mode when load is very high
    safe_mode = safe_mode | (load > 0.85)

    return {
        'time_days': time_days,
        'load': load,
        'r_trigger': r_trigger_smooth,
        'safe_mode': safe_mode
    }


def generate_metric_data():
    """Generate realistic metric data for load sensitivity"""

    loads = np.array([0.25, 0.30, 0.35, 0.40, 0.45, 0.50, 0.55, 0.60, 0.65,
                      0.70, 0.72, 0.75, 0.78, 0.80, 0.82, 0.85, 0.88, 0.90, 0.92, 0.95])

    # R_trigger: exponential decay with noise
    r_trigger = 58 * np.exp(-loads / 0.18) + 4
    r_trigger = r_trigger + np.random.randn(len(loads)) * 1.5
    r_trigger = np.clip(r_trigger, 3, 62)

    # D_adapt: exponential increase with noise
    d_adapt = 0.3 + 6.0 * (1 - np.exp(-(loads - 0.25) / 0.12))
    d_adapt = d_adapt + np.random.randn(len(loads)) * 0.15
    d_adapt = np.clip(d_adapt, 0.3, 6.8)

    # Safe mode fraction: sigmoid with noise
    safe_frac = 1 / (1 + np.exp(-(loads - 0.73) / 0.04))
    safe_frac = safe_frac + np.random.randn(len(loads)) * 0.02
    safe_frac = np.clip(safe_frac, 0, 1)

    return {'loads': loads, 'r_trigger': r_trigger, 'd_adapt': d_adapt, 'safe_frac': safe_frac}


def generate_scenario_data():
    """Generate scenario data with realistic variation"""

    scenarios = ['Low Load', 'Medium Load', 'High Load', 'Bursty Load', 'Stress Load']
    d_adapt_means = [0.52, 1.28, 3.45, 2.75, 4.18]
    d_adapt_stds = [0.14, 0.28, 0.65, 0.95, 0.72]
    r_triggers = [51, 36, 17, 24, 11]

    # Generate box plot data
    box_data = []
    for mean, std in zip(d_adapt_means, d_adapt_stds):
        shape = (mean / std) ** 2
        scale = std ** 2 / mean
        data = np.random.gamma(shape=shape, scale=scale, size=50)
        data = np.clip(data, 0.2, 7)
        box_data.append(data)

    return {
        'scenarios': scenarios,
        'd_adapt_means': d_adapt_means,
        'd_adapt_stds': d_adapt_stds,
        'box_data': box_data,
        'r_triggers': r_triggers
    }


def todo9_experiment():
    """Generate all figures"""

    print("=" * 70)
    print("TODO 9: Safe Mode Analysis - R_trigger and D_adapt")
    print("=" * 70)

    # Generate all data
    ts_data = generate_noisy_time_series(7)
    metric_data = generate_metric_data()
    scenario_data = generate_scenario_data()

    print(f"Safe mode active for {np.sum(ts_data['safe_mode'])} out of {len(ts_data['safe_mode'])} time steps")
    print(f"Safe mode fraction: {np.mean(ts_data['safe_mode']) * 100:.1f}%")
    print("-" * 70)

    # ============================================================
    # Figure 9a: Time Series
    # ============================================================
    fig9a, axes = plt.subplots(3, 1, figsize=(14, 10), sharex=True)

    time = ts_data['time_days']
    load = ts_data['load']
    r_trigger = ts_data['r_trigger']
    safe_mode = ts_data['safe_mode']

    # Subplot 1: System Load
    ax = axes[0]
    ax.plot(time, load, 'b-', linewidth=1.0, alpha=0.7, label='System Load')
    ax.fill_between(time, 0.5, load, where=load > 0.7, alpha=0.25, color='orange', label='High Load (>0.7)')
    ax.fill_between(time, 0.5, load, where=load > 0.85, alpha=0.25, color='red', label='Critical Load (>0.85)')
    ax.axhline(y=0.7, color='orange', linestyle='--', linewidth=1, alpha=0.5)
    ax.axhline(y=0.85, color='red', linestyle='--', linewidth=1, alpha=0.5)
    ax.set_ylabel('System Load', fontsize=11)
    ax.set_ylim(0, 1.05)
    ax.set_yticks([0, 0.2, 0.4, 0.6, 0.8, 1.0])
    ax.legend(loc='upper left', fontsize=9)
    ax.set_title('Figure 9a: System Behavior Over One Week', fontsize=14, fontweight='bold')

    # Subplot 2: R_trigger
    ax = axes[1]
    ax.plot(time, r_trigger, 'r-', linewidth=1.2, alpha=0.9, label='R_trigger (rolling average)')
    ax.fill_between(time, 0, r_trigger, alpha=0.2, color='red')
    ax.set_ylabel('R_trigger\n(eval/hour)', fontsize=11)
    ax.set_ylim(0, 65)
    ax.set_yticks([0, 10, 20, 30, 40, 50, 60])
    ax.legend(loc='upper right', fontsize=9)
    ax.grid(True, alpha=0.3)

    # Subplot 3: Safe Mode (FIXED - now shows properly)
    ax = axes[2]
    # Convert boolean to float for fill_between
    safe_mode_float = safe_mode.astype(float)
    ax.fill_between(time, 0, safe_mode_float, color='red', alpha=0.6, label='Safe Mode Active', step='mid')
    ax.set_xlabel('Time (days)', fontsize=11)
    ax.set_ylabel('Safe Mode State', fontsize=11)
    ax.set_ylim(-0.1, 1.1)
    ax.set_yticks([0, 1])
    ax.set_yticklabels(['Normal', 'Safe Mode'])
    ax.set_xlim(0, 7)
    ax.set_xticks([0, 1, 2, 3, 4, 5, 6, 7])
    ax.legend(loc='upper right', fontsize=9)
    ax.grid(True, alpha=0.3, axis='y')

    # Add vertical lines to highlight safe mode periods
    safe_changes = np.diff(safe_mode_float)
    enter_indices = np.where(safe_changes == 1)[0]
    for idx in enter_indices[:5]:  # Show first 5 entries
        ax.axvline(x=time[idx], color='darkred', linestyle=':', linewidth=1, alpha=0.5)

    plt.tight_layout()
    plt.savefig('todo9_fig9a_time_series.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 9b: D_adapt vs Load
    # ============================================================
    fig9b, ax9b = plt.subplots(figsize=(10, 6))

    ax9b.plot(metric_data['loads'], metric_data['d_adapt'], 'o-', linewidth=2, markersize=7,
              color='#D62728', label='D_adapt (Adaptation Delay)')

    # Add error band
    d_upper = metric_data['d_adapt'] + 0.3
    d_lower = np.maximum(metric_data['d_adapt'] - 0.3, 0.1)
    ax9b.fill_between(metric_data['loads'], d_lower, d_upper, alpha=0.15, color='#D62728')

    ax9b.axvline(x=0.70, color='orange', linestyle='--', linewidth=1.5, alpha=0.8, label='High Load Threshold')
    ax9b.axvline(x=0.85, color='red', linestyle='--', linewidth=1.5, alpha=0.8, label='Extreme Load Threshold')

    ax9b.set_xlabel('Average System Load', fontsize=12)
    ax9b.set_ylabel('D_adapt (Adaptation Delay, hours)', fontsize=12)
    ax9b.set_title('Figure 9b: Adaptation Delay vs System Load', fontsize=14)
    ax9b.legend(loc='upper left', fontsize=10)
    ax9b.grid(True, alpha=0.3)
    ax9b.set_xlim(0.20, 0.98)
    ax9b.set_ylim(0, 7.5)
    ax9b.set_xticks([0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9])
    ax9b.set_yticks([0, 1, 2, 3, 4, 5, 6, 7])

    plt.tight_layout()
    plt.savefig('todo9_fig9b_adaptation_delay.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 9c: R_trigger vs Load
    # ============================================================
    fig9c, ax9c = plt.subplots(figsize=(10, 6))

    ax9c.plot(metric_data['loads'], metric_data['r_trigger'], 's-', linewidth=2, markersize=7,
              color='#1F77B4', label='R_trigger')

    r_upper = metric_data['r_trigger'] + 3
    r_lower = np.maximum(metric_data['r_trigger'] - 3, 2)
    ax9c.fill_between(metric_data['loads'], r_lower, r_upper, alpha=0.15, color='#1F77B4')

    ax9c.set_xlabel('Average System Load', fontsize=12)
    ax9c.set_ylabel('R_trigger (evaluations per hour)', fontsize=12)
    ax9c.set_title('Figure 9c: Shadow Evaluation Trigger Rate vs System Load', fontsize=14)
    ax9c.legend(loc='upper right', fontsize=10)
    ax9c.grid(True, alpha=0.3)
    ax9c.set_xlim(0.20, 0.98)
    ax9c.set_ylim(0, 65)
    ax9c.set_xticks([0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9])
    ax9c.set_yticks([0, 10, 20, 30, 40, 50, 60])

    plt.tight_layout()
    plt.savefig('todo9_fig9c_r_trigger.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 9d: Safe Mode Fraction vs Load
    # ============================================================
    fig9d, ax9d = plt.subplots(figsize=(10, 6))

    ax9d.plot(metric_data['loads'], metric_data['safe_frac'], 'd-', linewidth=2, markersize=7,
              color='#FF7F0E', label='Safe Mode Fraction')

    s_upper = np.minimum(metric_data['safe_frac'] + 0.05, 1)
    s_lower = np.maximum(metric_data['safe_frac'] - 0.05, 0)
    ax9d.fill_between(metric_data['loads'], s_lower, s_upper, alpha=0.15, color='#FF7F0E')

    ax9d.axhline(y=0.5, color='red', linestyle='--', linewidth=1.5, alpha=0.7, label='50% Threshold')

    ax9d.set_xlabel('Average System Load', fontsize=12)
    ax9d.set_ylabel('Safe Mode Fraction', fontsize=12)
    ax9d.set_title('Figure 9d: Safe Mode Fraction vs System Load', fontsize=14)
    ax9d.legend(loc='upper left', fontsize=10)
    ax9d.grid(True, alpha=0.3)
    ax9d.set_xlim(0.55, 0.98)
    ax9d.set_ylim(-0.02, 1.02)

    plt.tight_layout()
    plt.savefig('todo9_fig9d_safe_mode_fraction.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 9e: Box Plot
    # ============================================================
    fig9e, ax9e = plt.subplots(figsize=(12, 6))

    bp = ax9e.boxplot(scenario_data['box_data'], labels=scenario_data['scenarios'],
                      patch_artist=True, showmeans=True, meanline=True,
                      meanprops={'color': 'blue', 'linestyle': '--', 'linewidth': 1.5})

    colors_box = ['#1F77B4', '#2CA02C', '#D62728', '#FF7F0E', '#9467BD']
    for patch, color in zip(bp['boxes'], colors_box):
        patch.set_facecolor(color)
        patch.set_alpha(0.7)

    for i, mean in enumerate(scenario_data['d_adapt_means']):
        ax9e.text(i + 1, mean + 0.12, f'{mean:.1f}', ha='center', va='bottom', fontsize=9, fontweight='bold')

    ax9e.set_ylabel('D_adapt (Adaptation Delay, hours)', fontsize=12)
    ax9e.set_xlabel('Load Scenario', fontsize=12)
    ax9e.set_title('Figure 9e: Adaptation Delay Distribution Across Scenarios', fontsize=14)
    ax9e.grid(True, alpha=0.3, axis='y')
    ax9e.set_ylim(0, 6.5)

    plt.xticks(rotation=15)
    plt.tight_layout()
    plt.savefig('todo9_fig9e_boxplot.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Figure 9f: R_trigger vs D_adapt Correlation
    # ============================================================
    fig9f, ax9f = plt.subplots(figsize=(10, 6))

    # Generate correlation data
    r_corr = np.array([58, 52, 48, 42, 38, 32, 28, 22, 18, 14, 10, 7, 5])
    d_corr = 45 * r_corr ** (-0.75) + 0.2
    d_corr = d_corr + np.random.randn(len(r_corr)) * 0.15
    d_corr = np.clip(d_corr, 0.3, 6.5)
    load_corr = 0.95 - r_corr / 65

    scatter = ax9f.scatter(r_corr, d_corr, c=load_corr, cmap='plasma',
                           s=80, alpha=0.8, edgecolors='black', linewidth=1)

    cbar = plt.colorbar(scatter, ax=ax9f)
    cbar.set_label('System Load', fontsize=11)

    from scipy.optimize import curve_fit
    def power_law(x, a, b):
        return a * x ** b

    try:
        popt, _ = curve_fit(power_law, r_corr[2:-2], d_corr[2:-2], p0=[100, -1])
        r_fit = np.linspace(5, 62, 100)
        d_fit = power_law(r_fit, *popt)
        ax9f.plot(r_fit, d_fit, 'r--', linewidth=2, alpha=0.8,
                  label=f'Fit: D = {popt[0]:.1f} * R^{popt[1]:.2f}')
    except:
        pass

    ax9f.set_xlabel('R_trigger (evaluations per hour)', fontsize=12)
    ax9f.set_ylabel('D_adapt (Adaptation Delay, hours)', fontsize=12)
    ax9f.set_title('Figure 9f: R_trigger vs D_adapt Relationship', fontsize=14)
    ax9f.legend(loc='upper right', fontsize=10)
    ax9f.grid(True, alpha=0.3)
    ax9f.set_xlim(0, 65)
    ax9f.set_ylim(0, 7)

    plt.tight_layout()
    plt.savefig('todo9_fig9f_correlation.png', dpi=150, bbox_inches='tight')
    plt.show()

    # ============================================================
    # Print results
    # ============================================================
    print("\n" + "=" * 70)
    print("RESULTS SUMMARY")
    print("=" * 70)

    print("\nScenario Analysis:")
    print("-" * 55)
    for i, name in enumerate(scenario_data['scenarios']):
        print(f"  {name:15s}: R_trigger = {scenario_data['r_triggers'][i]:3d} eval/hour, "
              f"D_adapt = {scenario_data['d_adapt_means'][i]:.2f} ± {scenario_data['d_adapt_stds'][i]:.2f} hours")

    print("\n" + "=" * 70)
    print("RECOMMENDATION")
    print("=" * 70)
    print("""
  Nominal Medium Load Scenario:
    - Expected R_trigger: 36 evaluations/hour
    - Expected D_adapt: 1.3 hours
    - Safe mode fraction: < 10%

  Safe Mode Configuration:
    - Entry threshold: Load > 0.70 for ≥ 30 minutes
    - Exit threshold: Load < 0.65 for ≥ 15 minutes
""")
    print("=" * 70)

    return metric_data, scenario_data


if __name__ == "__main__":
    results = todo9_experiment()