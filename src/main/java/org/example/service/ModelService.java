package org.example.service;

import org.example.util.DBConnector;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ModelService<T extends ModelObject> {

    Connection connection = DBConnector.getConnection();

    Optional<T> findById(String id);
    Optional<T> findByAttribute(String attr, String value);
    List<T> findAll();

    void save(T t);
}
