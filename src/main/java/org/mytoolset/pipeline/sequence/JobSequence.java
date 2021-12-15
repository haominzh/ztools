package org.mytoolset.pipeline.sequence;


import org.mytoolset.pipeline.DataSupplier;
import org.mytoolset.pipeline.JobStep;
import javax.annotation.Nonnull;

/**
 * A job sequence should have one or more job steps, which can
 * convert the original data with type M to the final result with
 * type N.
 *
 * @param <M> Original data type
 * @param <N> Target data type
 * @param <J> The job pipeline type
 */
public interface JobSequence<M, N, J> {

    /**
     * Append a new job step
     *
     * @param step The new step. The input type MUST match the current job sequence's output
     * @param <V> The data type, which the new step will convert to
     * @return A new job sequence, which will convert data from M to V
     */
    @Nonnull
    <V> JobSequence<M, V, J> andThen(@Nonnull JobStep<N, V> step);

    /**
     * Build job pipeline with the given data supplier
     *
     * @param dataSupplier The data supplier, which will provide data of type M
     * @return The job pipeline
     */
    @Nonnull
    J buildPipelineWithDataSupplier(@Nonnull DataSupplier<M> dataSupplier);
}
