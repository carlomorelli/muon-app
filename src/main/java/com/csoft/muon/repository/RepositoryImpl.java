package com.csoft.muon.repository;

import java.util.Map;
import java.util.stream.Collectors;

import com.csoft.muon.dao.Dao;
import com.csoft.muon.dao.DaoException;
import com.csoft.muon.domain.Item;

public class RepositoryImpl implements Repository {

    private Dao dao;
    
    @Override
    public Item get(int index) throws RepositoryException {
        return dao.fetchAllItems()
                .stream()
                .filter(item -> item.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new RepositoryException("Unable to find item with given index " + index));
    }

    @Override
    public Map<Integer, Item> getAll() {
        return dao.fetchAllItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getIndex(), item -> item));
    }

    @Override
    public void append(Item item) throws RepositoryException {
        try {
            dao.insertItem(item);
        } catch (DaoException e) {
            throw new RepositoryException("Unable to insert item: forbidden action (e.g. index null or negative)");
        }
    }

    @Override
    public void update(Item item, int index) throws RepositoryException {
        try {
            dao.updateItem(item);
        } catch (DaoException e) {
            throw new RepositoryException("Unable to update item: given index does not match");
        }
    }

    @Override
    public void delete(int index) throws RepositoryException {
        try {
            dao.fetchItemAtIndex(index);
        } catch (DaoException e) {
            throw new RepositoryException("Unable to delete item: unable to find item at given index");
        } 
        dao.deleteItemAtIndex(index);
    }

}
