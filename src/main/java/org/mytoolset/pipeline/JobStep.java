package org.mytoolset.pipeline;

import org.mytoolset.pipeline.context.JobContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A JobStep is one step in the JobPipeline. The responsibility is to
 * convert input data with type T to output data with type R.
 *
 * @param <T> The input data type
 * @param <R> The output data type
 */
public interface JobStep<T, R> {
    @Nullable
    R execute(@Nonnull T input, @Nonnull JobContext context);

    @Nonnull
    default String getJobStepName() {
        return getClass().getSimpleName();
    }
}
