package org.mytoolset.pipeline.sequence;


import org.mytoolset.pipeline.DataSupplier;
import org.mytoolset.pipeline.JobPipeline;
import org.mytoolset.pipeline.JobStep;
import javax.annotation.Nonnull;

/**
 * SimpleJobSequence contains a list of JobSteps, which are intended to be
 * executed sequentially.
 *
 * @param <M> The starting data type of the sequence
 * @param <N> The target data type of the sequence
 */
public class SimpleJobSequence<M, N> extends AbstractJobSequence<M, N, JobPipeline> {

    private SimpleJobSequence() {
    }

    /**
     * The static factory method to instantiate a JobSequence with the fist step
     *
     * @param firstStep The JobStep object for the first step
     * @param <K> The starting data type of the sequence
     * @param <L> The target data type of the sequence
     * @return The JobSequence object having starting data type K to target data type L
     */
    @Nonnull
    public static <K, L> JobSequence<K, L, JobPipeline> startFrom(@Nonnull JobStep<K, L> firstStep) {
        SimpleJobSequence<K, L> sequence = new SimpleJobSequence<>();
        sequence.init(firstStep);
        return sequence;
    }

    /**
     * Instantiate a JobPipeLine object with the given data supplier
     *
     * @param dataSupplier The data supplier, which will provide data of type M
     * @return The JobPipeline object
     */
    @Nonnull
    @Override
    public JobPipeline buildPipelineWithDataSupplier(@Nonnull DataSupplier<M> dataSupplier) {
        return new JobPipeline(dataSupplier, steps);
    }

    @Nonnull
    @Override
    protected <V> AbstractJobSequence<M, V, JobPipeline> newInstance() {
        return new SimpleJobSequence<>();
    }
}
