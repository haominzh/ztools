package org.mytoolset.pipeline.proxy;

import org.mytoolset.pipeline.JobStep;
import org.mytoolset.pipeline.context.JobContextModifier;
import java.lang.reflect.Method;
import java.time.Instant;

public class JobStepProxy extends AbstractPipelineProxy<JobStep<?, ?>> {

    public JobStepProxy(JobStep<?, ?> target, JobContextModifier contextModifier) {
        super(target, contextModifier);
    }

    @Override
    public void before(Method method, Object[] args) {
        contextModifier.reportStartedAt(target.getJobStepName(), Instant.now());
    }

    @Override
    public void after(Method method, Object[] args) {
        contextModifier.reportFinishedAt(target.getJobStepName(), Instant.now());
    }
}
