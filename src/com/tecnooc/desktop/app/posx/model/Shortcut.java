/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_shortcut")
public class Shortcut implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "key")
    private String key;
    @Basic(optional = false)
    @Column(name = "shift")
    private boolean shift;
    @Basic(optional = false)
    @Column(name = "alt")
    private boolean alt;
    @Basic(optional = false)
    @Column(name = "ctrl")
    private boolean ctrl;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;

    public Shortcut() {
    }

    public Shortcut(Integer id) {
        this.id = id;
    }

    public Shortcut(Integer id, String name, String key, boolean shift, boolean alt, boolean ctrl, boolean active) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.shift = shift;
        this.alt = alt;
        this.ctrl = ctrl;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getShift() {
        return shift;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public boolean getAlt() {
        return alt;
    }

    public void setAlt(boolean alt) {
        this.alt = alt;
    }

    public boolean getCtrl() {
        return ctrl;
    }

    public void setCtrl(boolean ctrl) {
        this.ctrl = ctrl;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shortcut)) {
            return false;
        }
        Shortcut other = (Shortcut) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Shortcut[ id=" + id + " ]";
    }
    
}
