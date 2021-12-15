package org.mytoolset.pipeline.sequence;

import org.mytoolset.pipeline.JobStep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

abstract class AbstractJobSequence<M, N, J> implements JobSequence<M, N, J>{
    protected List<JobStep<?,?>> steps;

    @Nonnull
    public <V> JobSequence<M, V, J> andThen(@Nonnull JobStep<N, V> step) {
        AbstractJobSequence<M, V, J> sequence = newInstance();
        sequence.steps = new ArrayList<>(this.steps);
        sequence.steps.add(step);
        return sequence;
    }

    protected <K, L> void init(@Nonnull JobStep<K, L> firstStep) {
        this.steps = Collections.singletonList(firstStep);
    }

    @Nonnull
    protected abstract <V> AbstractJobSequence<M, V, J> newInstance();
}
