package com.csoft.muon.lib;

import java.util.List;
import java.util.Objects;

public class Item {

    private Integer index;
    private String label;
    private List<Integer> array;

    public Item(Integer index, String label) {
        this.index = index;
        this.label = label;
        this.array = null;
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

    public boolean isValid() {
        return index != null && !label.isEmpty() && !array.isEmpty();
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
