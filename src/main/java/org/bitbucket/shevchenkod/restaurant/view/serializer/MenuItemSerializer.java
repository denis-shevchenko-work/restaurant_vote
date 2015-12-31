package org.bitbucket.shevchenkod.restaurant.view.serializer;

import org.bitbucket.shevchenkod.restaurant.model.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class MenuItemSerializer extends PublicViewSerializer<MenuItem> {
    @Override
    public Object serialize(MenuItem value) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", value.getId());
        result.put("dish_name", value.getDish().getName());
        result.put("price", value.getPrice());
        return result;
    }
}