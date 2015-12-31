package org.bitbucket.shevchenkod.restaurant.view.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DishDto implements Serializable {
	private Long id;
	private String name;

	public DishDto(String name){
		this.name = name;
	}

}
