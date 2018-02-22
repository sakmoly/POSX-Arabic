package com.tecnooc.desktop.app.posx.dto;

import java.util.Objects;

/**
 *
 * @author jomit
 */
public abstract class Dto<T> {
    protected T entity;

    public Dto(T entity) {
        this.entity = entity;
    }
    
    public T getEntity() {
        return entity;
    }   
}
