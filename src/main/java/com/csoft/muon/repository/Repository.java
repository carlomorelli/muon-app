package com.csoft.muon.repository;

import java.util.List;

import com.csoft.muon.domain.Item;

/**
 * Repository interface
 * @author Carlo Morelli
 *
 */
public interface Repository {

    List<Item> fetchAllItems();

    Item fetchItemAtIndex(int index) throws RepositoryException;
    
    void insertItem(Item item) throws RepositoryException;

    void updateItem(Item item) throws RepositoryException;

    void deleteItemAtIndex(int index) throws RepositoryException;

}
