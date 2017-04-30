package com.csoft.muon.repository;

import java.util.Map;

import com.csoft.muon.domain.Item;

public interface Repository {

    Item get(int index) throws RepositoryException;
    
    Map<Integer, Item> getAll();
    
    void append(Item item) throws RepositoryException;
    
    void update(Item item, int index) throws RepositoryException;
    
    void delete(int index) throws RepositoryException;
    
}
