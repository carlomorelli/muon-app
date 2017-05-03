package com.csoft.muon.repository;

import java.util.Map;
import java.util.stream.Collectors;

import com.csoft.muon.dao.Dao;
import com.csoft.muon.dao.DaoException;
import com.csoft.muon.domain.Item;

public class RepositoryImpl implements Repository {

    private Dao dao;
    private Map<Integer, Item> registry;
    
    public RepositoryImpl() {
        // initialization: fetch all existing database content, and put into registry
        registry = dao.fetchAllItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getIndex(), item -> item));
    }
    
    @Override
    public Item get(int index) throws RepositoryException {
        if (!registry.containsKey(index)) {
            throw new RepositoryException("Unable to get item: given index is not used");
        }
        return registry.get(index);
    }

    @Override
    public Map<Integer, Item> getAll() {
        return registry;
    }

    @Override
    public void append(Item item) throws RepositoryException {
        if (registry.containsKey(item.getIndex())) {
            throw new RepositoryException("Unable to insert item: given index is already used.");
        }
        try {
            dao.insertItem(item);
        } catch (DaoException e) {
            throw new RepositoryException("Unable to insert item: forbidden action (e.g. index null or negative).", e);
        }
        // only with success, update registry
        registry.put(item.getIndex(), item);
    }

    @Override
    public void update(Item item, int index) throws RepositoryException {
        if (index != item.getIndex()) {
            throw new RepositoryException("Unable to update item: given index does not match item contents.");
        }
        if (!registry.containsKey(index)) {
            throw new RepositoryException("Unable to update item: given index is not used.");
        };
        try {
            dao.updateItem(item);
        } catch (DaoException e) {
            throw new RepositoryException("Unable to update item: given index is not used.", e);
        }
        // only with success, update registry
        registry.put(item.getIndex(), item);
    }

    @Override
    public void delete(int index) throws RepositoryException {
        try {
            dao.deleteItemAtIndex(index);
        } catch (DaoException e) {
            throw new RepositoryException("Unable to delete item: given index is not used.", e);
        } 
        // only with success, update registry
        registry.remove(index);
    }

}
