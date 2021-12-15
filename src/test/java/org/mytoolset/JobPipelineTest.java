package org.mytoolset;

import org.mytoolset.pipeline.DataSupplier;
import org.mytoolset.pipeline.JobPipeline;
import org.mytoolset.pipeline.JobStep;
import org.mytoolset.pipeline.LastStep;
import org.mytoolset.pipeline.context.ExecutionStats;
import org.mytoolset.pipeline.context.JobContext;
import org.mytoolset.pipeline.exceptions.PipelineExecutionException;
import org.mytoolset.pipeline.sequence.SimpleJobSequence;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for JobPipeline.
 */
public class JobPipelineTest {

    @Test(expected = PipelineExecutionException.class)
    public void shouldThrowException_whenIntermediaStepHasNullInput() {
        JobStep<Integer, Void> step1 = new JobStep<Integer, Void>() {
            @Nullable
            @Override
            public Void execute(@Nonnull Integer input, @Nonnull JobContext context) {
                return null;
            }
        };

        JobStep<Void, Integer> step2 = new JobStep<Void, Integer>() {
            @Nullable
            @Override
            public Integer execute(@Nonnull Void input, @Nonnull JobContext context) {
                return null;
            }
        };
        DataSupplier<Integer> dataSupplier = () -> ThreadLocalRandom.current().nextInt();

        SimpleJobSequence.startFrom(step1)
                         .andThen(step2)
                         .buildPipelineWithDataSupplier(dataSupplier)
                         .run();
    }

    @Test
    public void orchestrationTest_fromStringList_toIntegerList_toInteger() {
        List<String> inputData = Arrays.asList("1", "12", "123");
        int expectedOutput = inputData.stream()
                                      .mapToInt(String::length)
                                      .sum();
        AtomicInteger resultHolder = new AtomicInteger();
        JobStep<List<String>, List<Integer>> step1 = (input, context) -> {
            // return a list of string length
            return input.stream()
                        .map(String::length)
                        .collect(Collectors.toList());
        };

        JobStep<List<Integer>, Integer> step2 = (input, context) -> input.stream().mapToInt(
                Integer::intValue).sum();

        LastStep<Integer> lastStep = (input, context) -> resultHolder.set(input);

        DataSupplier<List<String>> dataSupplier = () -> inputData;

        SimpleJobSequence.startFrom(step1)
                         .andThen(step2)
                         .andThen(lastStep)
                         .buildPipelineWithDataSupplier(dataSupplier)
                         .run();
        Assert.assertEquals(expectedOutput, resultHolder.get());
    }

    @Test
    public void executionStatsShouldBeAutoInjected() {
        String stepName1 = "testStep1";
        String stepName2 = "testStep2";
        String dataSupplierName = "testDataSupplier";
        JobStep<List<String>, List<Integer>> step1 = new JobStep<List<String>, List<Integer>>() {
            @Override
            public List<Integer> execute(@Nonnull List<String> input, @Nonnull JobContext context) {
                return input.stream()
                            .map(String::length)
                            .collect(Collectors.toList());
            }

            @Nonnull
            @Override
            public String getJobStepName() {
                return stepName1;
            }
        };

        JobStep<List<Integer>, Integer> step2 = new JobStep<List<Integer>, Integer>() {
            @Override
            public Integer execute(@Nonnull List<Integer> input, @Nonnull JobContext context) {
                return input.stream().mapToInt(Integer::intValue).sum();
            }

            @Nonnull
            @Override
            public String getJobStepName() {
                return stepName2;
            }
        };

        DataSupplier<List<String>> dataSupplier = new DataSupplier<List<String>>() {
            @Override
            public List<String> get() {
                return Arrays.asList("test1", "test2");
            }

            @Nonnull
            @Override
            public String getDataSupplierName() {
                return dataSupplierName;
            }
        };

        JobPipeline pipeline = SimpleJobSequence.startFrom(step1)
                                                .andThen(step2)
                                                .buildPipelineWithDataSupplier(dataSupplier);

        pipeline.run();

        ExecutionStats step1Stats = pipeline.getJobContext().getExecutionStats(step1.getJobStepName());
        Assert.assertNotNull(step1Stats);
        Assert.assertNotNull(step1Stats.getStartedAt());
        Assert.assertNotNull(step1Stats.getFinishedAt());

        ExecutionStats step2Stats = pipeline.getJobContext().getExecutionStats(step2.getJobStepName());
        Assert.assertNotNull(step2Stats);
        Assert.assertNotNull(step2Stats.getStartedAt());
        Assert.assertNotNull(step2Stats.getFinishedAt());

        ExecutionStats dataSupplierStats = pipeline.getJobContext().getExecutionStats(dataSupplier.getDataSupplierName());
        Assert.assertNotNull(dataSupplierStats);
        Assert.assertNotNull(dataSupplierStats.getStartedAt());
        Assert.assertNotNull(dataSupplierStats.getFinishedAt());
    }
}
