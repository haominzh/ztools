package org.mytoolset.pipeline.context;

import java.time.Instant;
import javax.annotation.Nonnull;

/**
 * Interface to update JobContext
 */
public interface JobContextModifier extends JobContext {

    /**
     * Report starting timestamp
     * @param name The name of the executor
     * @param timestamp the starting timestamp
     */
    void reportStartedAt(@Nonnull String name, @Nonnull Instant timestamp);

    /**
     * Report finishing timestamp
     * @param name The name of the executor
     * @param timestamp the finish timestamp
     */
    void reportFinishedAt(@Nonnull String name, @Nonnull Instant timestamp);
}
