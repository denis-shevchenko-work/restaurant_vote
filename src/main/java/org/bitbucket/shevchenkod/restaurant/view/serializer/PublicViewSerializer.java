package org.bitbucket.shevchenkod.restaurant.view.serializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PublicViewSerializer<T> {
    public abstract Object serialize(T value);

    public Object serialize(Collection<T> value) {
        List<Object> result = new ArrayList<Object>();

        if (value != null){
            for (T each : value) {
                result.add(serialize(each));
            }
        }

        return result;
    }
}