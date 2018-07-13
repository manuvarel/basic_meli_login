package com.melilogin.demo.data.persistence.dao.interfaces.base;

import java.io.Serializable;

public interface BaseDAO<E extends Serializable> extends BaseReadOnlyDAO<E> {

    public abstract void create(E entity);

    public abstract void update(E entity);

    public abstract void delete(E entity);

    public abstract void delete(Integer id);

}