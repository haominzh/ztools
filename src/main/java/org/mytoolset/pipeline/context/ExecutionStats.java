package org.mytoolset.pipeline.context;

import java.time.Instant;
import javax.annotation.Nullable;

/**
 * The execution stats interface
 */
public interface ExecutionStats {

    /**
     * Get started timestamp
     * @return started timestamp
     */
    @Nullable
    Instant getStartedAt();

    /**
     * Get finished timestamp
     * @return finished timestamp
     */
    @Nullable
    Instant getFinishedAt();
}
