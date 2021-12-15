package org.mytoolset.pipeline.context;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The default JobContextModifier implementation
 */
public class DefaultJobContext implements JobContextModifier {
    private final UUID jobId;
    private final Map<String, ExecutionStats> executionStats;

    public DefaultJobContext() {
        jobId = UUID.randomUUID();
        executionStats = new HashMap<>();
    }

    @Nonnull
    @Override
    public UUID getJobId() {
        return jobId;
    }

    @Nullable
    @Override
    public ExecutionStats getExecutionStats(@Nonnull String name) {
        return executionStats.get(name);
    }

    @Nonnull
    @Override
    public Map<String, ExecutionStats> getAllExecutionStats() {
        return executionStats;
    }

    @Override
    public void reportStartedAt(@Nonnull String name, @Nonnull Instant timestamp) {
        this.executionStats.put(name, new DefaultExecutionStats(timestamp, null));
    }

    @Override
    public void reportFinishedAt(@Nonnull String name, @Nonnull Instant timestamp) {
        ExecutionStats stats = this.executionStats.get(name);
        if (stats != null) {
            this.executionStats.put(name, new DefaultExecutionStats(stats.getStartedAt(), timestamp));
        }
    }
}
