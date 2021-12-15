package org.mytoolset.pipeline.context;

import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provide information of the running pipeline
 */
public interface JobContext {

    /**
     * Get the jobId of the JobPipeline object
     * @return The job UUID
     */
    @Nonnull
    UUID getJobId();

    /**
     * Get execution stats.
     * @param name Could be a DataSupplier name, or a JobStep name
     * @return The corresponding ExecutionStats
     */
    @Nullable
    ExecutionStats getExecutionStats(@Nonnull String name);

    /**
     * Get all execution stats
     * @return A map of ExecutionStats
     */
    @Nonnull
    Map<String, ExecutionStats> getAllExecutionStats();
}
