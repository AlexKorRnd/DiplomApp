package com.alexkorrnd.diplomapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseEntityMapper<F, T> {

    @Nullable
    public abstract T from(F entity);

    @NonNull
    public List<T> from(@Nullable Collection<F> entityCollection) {
        if (entityCollection == null || entityCollection.isEmpty()) {
            return Collections.emptyList();
        }
        final List<T> list = new ArrayList<T>(entityCollection.size());
        for (F entity : entityCollection) {
            final T model = from(entity);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    @Nullable
    public abstract F to(T model);

    @NonNull
    public List<F> to(@Nullable Collection<T> modelCollection) {
        if (modelCollection == null || modelCollection.isEmpty()) {
            return Collections.emptyList();
        }
        final List<F> list = new ArrayList<F>(modelCollection.size());
        for (T model : modelCollection) {
            final F entity = to(model);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }
}
