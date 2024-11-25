package org.example.service;

import java.util.List;

public interface ModelService<T extends ModelObject> {
    T findById(String id);
    List<T> findAll();

    void save(T t);
}
