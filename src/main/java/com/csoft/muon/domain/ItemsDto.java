package com.csoft.muon.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemsDto {

	private Integer total;
	private List<Item> items;
	
	public ItemsDto(Integer total, List<Item> items) {
		super();
		this.total = total;
		this.items = items;
	}

	public Integer getTotal() {
		return total;
	}

	public List<Item> getItems() {
		return items;
	}
	
	@JsonIgnore
    public boolean isValid() {
        return total != null && total > 0 &&
        		items != null && !items.isEmpty();
    }

	@Override
	public int hashCode() {
		return Objects.hash(total, items);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final ItemsDto other = (ItemsDto) obj;
		return Objects.equals(this.total, other.total) &&
				Objects.equals(this.items,  other.items);
	}
	
	
	
	
}
