package org.mytoolset.pipeline.proxy;

import org.mytoolset.pipeline.DataSupplier;
import org.mytoolset.pipeline.context.JobContextModifier;
import java.lang.reflect.Method;
import java.time.Instant;

public class DataSupplierProxy extends AbstractPipelineProxy<DataSupplier<?>> {

    public DataSupplierProxy(DataSupplier<?> target,
            JobContextModifier contextModifier) {
        super(target, contextModifier);
    }

    @Override
    public void before(Method method, Object[] args) {
        contextModifier.reportStartedAt(target.getDataSupplierName(), Instant.now());
    }

    @Override
    public void after(Method method, Object[] args) {
        contextModifier.reportFinishedAt(target.getDataSupplierName(), Instant.now());
    }
}
