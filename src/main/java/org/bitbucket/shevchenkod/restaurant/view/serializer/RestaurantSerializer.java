package org.bitbucket.shevchenkod.restaurant.view.serializer;

import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;

import java.util.HashMap;
import java.util.Map;

public class RestaurantSerializer extends PublicViewSerializer<Restaurant> {

	@Override
    public Object serialize(Restaurant value) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", value.getId());
        result.put("name", value.getName());
        return result;
    }
}