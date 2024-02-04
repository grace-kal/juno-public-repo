package com.master.services.interfaces;

import java.util.List;

public interface IBaseService<T> {
    List<T> getAll();

    T getById(Long id);

    T create(T entity);

    T update(T entity);

    boolean remove(long id);

}
