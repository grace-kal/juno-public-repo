package com.master.services;

import com.master.models.shared.BaseModel;
import com.master.services.interfaces.IBaseService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseModel> implements IBaseService<T> {
    protected abstract JpaRepository<T, Long> getRepo();

    public List<T> getAll() {
        return getRepo().findAll();
    }

    public T getById(Long id) {
        Optional<T> entity = getRepo().findById(id);
        return entity.orElse(null);
        //return entity.isPresent() ? entity.get() : null;
    }

    public T create(T entity) {
        return getRepo().save(entity);
    }

    public T update(T entity) {
        long id = entity.getId();
        Optional<T> entityInDb = getRepo().findById(id);
        return entityInDb.isPresent() ? getRepo().save(entity) : null;
    }

    public boolean remove(long id) {
        Optional<T> optionalEntity = getRepo().findById(id);
        if (optionalEntity.isPresent()) {
            getRepo().delete(optionalEntity.get());
            return true;
        }
        return false;
    }
//
//    public Page<User> list(Pageable pageable) {
//        return getRepo().findAll(pageable);
//    }
//
//    public Page<User> list(Pageable pageable, Specification<User> filter) {
//        return getRepo().findAll(filter, pageable);
//    }
}
