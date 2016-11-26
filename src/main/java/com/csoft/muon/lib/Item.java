package com.csoft.muon.lib;

import java.util.List;

import lombok.Data;

@Data
public class Item {

    private Integer index;
    private String label;
    private List<Integer> array;
    
    public boolean isValid() {
        return index != null && !label.isEmpty() && !array.isEmpty();
    }
}
