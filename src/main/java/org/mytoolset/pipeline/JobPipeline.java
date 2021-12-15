package org.mytoolset.pipeline;

import org.mytoolset.pipeline.context.DefaultJobContext;
import org.mytoolset.pipeline.context.JobContext;
import org.mytoolset.pipeline.context.JobContextModifier;
import org.mytoolset.pipeline.exceptions.PipelineExecutionException;
import org.mytoolset.pipeline.proxy.DataSupplierProxy;
import org.mytoolset.pipeline.proxy.JobStepProxy;
import org.mytoolset.pipeline.sequence.JobSequence;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The JobPipeline is the driver of JobSteps.It consists of a DataSupplier, a list of JobSteps,
 * and a JobContextModifier.
 */
public class JobPipeline implements Runnable {

    private final DataSupplier<?> dataSupplier;
    private final List<JobStep<?, ?>> steps;
    private final JobContextModifier contextModifier;

    public JobPipeline(DataSupplier<?> dataSupplier,
            List<JobStep<?, ?>> steps) {

        contextModifier = new DefaultJobContext();

        this.dataSupplier = (DataSupplier<?>) Proxy.newProxyInstance(
                JobPipeline.class.getClassLoader(),
                new Class[]{DataSupplier.class},
                new DataSupplierProxy(dataSupplier, contextModifier)
        );
        this.steps = steps.stream()
                          .map(s -> makeProxyInstance(s, contextModifier))
                          .collect(Collectors.toList());
    }

    private JobStep<?, ?> makeProxyInstance(JobStep<?, ?> step, JobContextModifier contextModifier) {
        return (JobStep<?, ?>) Proxy.newProxyInstance(
                JobSequence.class.getClassLoader(),
                new Class[]{JobStep.class},
                new JobStepProxy(step, contextModifier)
        );
    }

    public JobContext getJobContext() {
        return contextModifier;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void run() {
        Object source = this.dataSupplier.get();
        if (source != null) {
            Object target;
            for (JobStep p : steps) {
                if (source == null) {
                    throw new PipelineExecutionException(p.getJobStepName() + " cannot take null as input");
                }
                target = p.execute(source, contextModifier);
                source = target;
            }
        }
    }
}
