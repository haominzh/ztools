package org.mytoolset.pipeline.context;

import java.time.Instant;
import javax.annotation.Nullable;

/**
 * The default ExecutionStats implementation.
 */
public class DefaultExecutionStats implements ExecutionStats {
    private final Instant startedAt;
    private final Instant finishedAt;

    public DefaultExecutionStats(Instant startedAt, Instant finishedAt) {
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    @Nullable
    @Override
    public Instant getStartedAt() {
        return startedAt;
    }

    @Nullable
    @Override
    public Instant getFinishedAt() {
        return finishedAt;
    }
}
