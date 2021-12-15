package org.mytoolset.pipeline.proxy;

import java.lang.reflect.Method;

public interface PipelineProxy {
    void before(Method method, Object[] args);
    void after(Method method, Object[] args);
}
