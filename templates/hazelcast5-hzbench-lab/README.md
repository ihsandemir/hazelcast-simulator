# hzbench-equivalent Simulator Tests

Replicates the hazelcast-client-benchmark (hzbench) two-phase test suite using
Hazelcast Simulator, for comparison against hzbench lab results.

## Configuration

Matches hzbench defaults:
- 3 members, 1 client (loadgenerator)
- Cluster name: `hazelcast-benchmark`
- Map: `benchmark-map`, 100K keys
- Duration: 15 minutes per test, warmup: 150 seconds

## Before Running

1. Edit `inventory_plan.yaml` — set `user` to your SSH username
2. Edit `tests.yaml` — replace placeholder values:
   - `threadCount`: optimal thread count from your hzbench max-throughput runs
   - `ratePerSecond`: max_throughput / 5 from your hzbench runs
   - `version`: Hazelcast version to test (default: maven=5.6.0)

## Setup & Run

```shell
# Create environment from inventory plan
inventory apply

# Install Java and Simulator on all machines
inventory install java
inventory install simulator

# Optional: OS-level tuning (sysctl, etc.)
inventory tune

# Run all tests
perftest run
```

## Results

Results are collected in `runs/<test-name>/<timestamp>/`:
- HDR histograms with latency percentiles
- Throughput measurements
- Performance monitor data

## Parameter Mapping (hzbench -> Simulator)

| hzbench | Simulator |
|---------|-----------|
| 100% GET | `getProb: 1, putProb: 0` |
| 100% PUT | `getProb: 0, putProb: 1` |
| target_rate | `ratePerSecond` |
| threads | `threadCount` |
| duration 15min | `duration: 900s` |
| warmup min(5m, dur/6) | `warmup_seconds: 150` |

## Known Differences

- Key type: `Long` (simulator) vs `Integer` (hzbench) — negligible perf impact
- Rate limiter: `SleepingMetronome` vs `ScheduledExecutorService` — both avoid coordinated omission
- Value content: random bytes vs deterministic pattern — irrelevant for perf
