package com.csoft.muon.lib;

import java.util.List;

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
}
