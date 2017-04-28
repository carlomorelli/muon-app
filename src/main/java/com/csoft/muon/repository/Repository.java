package com.csoft.muon.repository;

import java.util.Map;

import com.csoft.muon.domain.Item;

public interface Repository {

    Item get(int index);
    
    Map<Integer, Item> getAll();
    
    void append(Item item);
    
    void update(Item item, int index);
    
    void delete(int index);
    
}
