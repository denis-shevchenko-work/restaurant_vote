package org.bitbucket.shevchenkod.restaurant.view.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonSerialize()
public class MenuDto implements Serializable {

	private static final long serialVersionUID = -2460804595743227994L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("restaurant_name")
	private String restaurantName;

	@JsonProperty("restaurant_id")
	private Long restaurantId;

	@JsonProperty("date")
	private Date Date = new Date();

	@JsonProperty("menu")
	private List<MenuItemDto> menu = new ArrayList<>();

	public MenuDto(String restaurantName) {
		this.restaurantName = restaurantName;
	}

}
