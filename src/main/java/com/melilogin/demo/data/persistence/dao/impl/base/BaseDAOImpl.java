package com.melilogin.demo.data.persistence.dao.impl.base;

import java.io.Serializable;

import com.google.common.base.Optional;
import com.melilogin.demo.data.persistence.dao.interfaces.base.BaseDAO;

public class BaseDAOImpl<E extends Serializable> extends BaseReadOnlyDAOImpl<E> implements BaseDAO<E> {
	
    public BaseDAOImpl() {
        em = getEntityManager();
    }

    @Override
    public void create(E entity) {
        em.persist(entity);
    }

    @Override
    public void update(E entity) {
        em.flush();
    }

    @Override
    public void delete(E entity) {
        em.remove(em.merge(entity));
    }

    @Override
    public void delete(Integer id) {
        Optional<E> entity = findById(id);
        em.remove(entity.get());
    }
}
