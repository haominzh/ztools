package org.mytoolset.pipeline;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a supplier of results.
 *
 * @param <T> the type of results supplied by this supplier
 */
public interface DataSupplier<T>{
    @Nullable
    T get();

    @Nonnull
    default String getDataSupplierName() {
        return getClass().getSimpleName();
    }
}
