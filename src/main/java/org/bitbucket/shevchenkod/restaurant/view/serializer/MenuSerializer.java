package org.bitbucket.shevchenkod.restaurant.view.serializer;

import org.bitbucket.shevchenkod.restaurant.model.Menu;

import java.util.HashMap;
import java.util.Map;

public class MenuSerializer extends PublicViewSerializer<Menu> {
	private final MenuItemSerializer menuItemSerializer;

	public MenuSerializer(MenuItemSerializer menuItemSerializer) {
		this.menuItemSerializer = menuItemSerializer;
	}

	@Override
    public Object serialize(Menu value) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", value.getId());
        result.put("restaurant_name", value.getRestaurant().getName());
        result.put("menu", menuItemSerializer.serialize(value.getItems()));
        return result;
    }
}