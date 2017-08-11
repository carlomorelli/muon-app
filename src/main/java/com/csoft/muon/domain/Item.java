package com.csoft.muon.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Main database entity
 * @author Carlo Morelli
 *
 */
@JsonInclude(Include.NON_NULL)
public class Item {

    private Integer index;
    private String label;
    private List<Integer> array; //optional

    public Item() {
    }

    public Item(Integer index, String label, List<Integer> array) {
        this.index = index;
        this.label = label;
        this.array = array;
    }
    
    public Item(Integer index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public Integer getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public List<Integer> getArray() {
        return array;
    }

    @JsonIgnore
    public boolean isValid() {
        return index != null && !label.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, label, array);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        return Objects.equals(this.index, other.index) 
                && Objects.equals(this.label, other.label)
                && Objects.equals(this.array,  other.array);
    }
    
    
}
