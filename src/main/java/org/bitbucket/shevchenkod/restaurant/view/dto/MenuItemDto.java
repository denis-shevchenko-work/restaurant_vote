package org.bitbucket.shevchenkod.restaurant.view.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuItemDto implements Serializable{


	@JsonProperty("dish_name")
	private String dishName;

	@JsonProperty("dish_id")
	private Long dishId;

	@JsonProperty("price")
	private BigDecimal price;

	public MenuItemDto(String name, BigDecimal price) {
		dishName = name;
		this.price = price;
	}

	public MenuItemDto(String dishName, Long dishId, BigDecimal price) {
		this.dishName = dishName;
		this.dishId = dishId;
		this.price = price;
	}

	public MenuItemDto() {
	}
}
