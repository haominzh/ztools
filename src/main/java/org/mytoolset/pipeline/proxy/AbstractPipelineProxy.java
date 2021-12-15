package org.mytoolset.pipeline.proxy;

import org.mytoolset.pipeline.context.JobContextModifier;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

abstract class AbstractPipelineProxy<T>  implements InvocationHandler, PipelineProxy {
    protected final JobContextModifier contextModifier;
    protected final T target;

    public AbstractPipelineProxy(T target, JobContextModifier contextModifier) {
        this.contextModifier = contextModifier;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before(method, args);
        Object result = method.invoke(target, args);
        after(method, args);
        return result;
    }
}
