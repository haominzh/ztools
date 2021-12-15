package org.mytoolset.pipeline;

import org.mytoolset.pipeline.context.JobContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This interface indicates the JobStep is the last step in the pipeline.
 * This is an "alias" interface of JobStep, which provides a convenience
 * method <strong>end</strong> to be overwritten, inorder to avoid <pre>return null</pre>
 * in the implementation class
 *
 * @param <T> The input data type
 */
public interface LastStep<T> extends JobStep<T, Void>{

    @Nullable
    @Override
    default Void execute(@Nonnull T input, @Nonnull JobContext context) {
        end(input, context);
        return null;
    }

    void end(@Nonnull T input, @Nonnull JobContext context);
}
