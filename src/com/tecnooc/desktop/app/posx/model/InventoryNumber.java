/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_inventory_numbers")
public class InventoryNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "logical_ref")
    private Long logicalRef;
    @Column(name = "serial_lot_num_ref")
    private Long serialLotNumRef;
    @Column(name = "type")
    private Integer type;
    @Column(name = "serial_lot_number")
    private String serialLotNumber;
    @Column(name = "serial_sold")
    private Integer serialSold;
    @Column(name = "lot_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date lotExpiryDate;
    @Column(name = "unit")
    private String unit;
    @Column(name = "active")
    private Boolean active;
    @JoinColumn(name = "item_sid", referencedColumnName = "item_sid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Inventory inventory;

    public InventoryNumber() {
    }

    public InventoryNumber(Long logicalRef) {
        this.logicalRef = logicalRef;
    }

    public Long getLogicalRef() {
        return logicalRef;
    }

    public void setLogicalRef(Long logicalRef) {
        this.logicalRef = logicalRef;
    }

    public Long getSerialLotNumRef() {
        return serialLotNumRef;
    }

    public void setSerialLotNumRef(Long serialLotNumRef) {
        this.serialLotNumRef = serialLotNumRef;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSerialLotNumber() {
        return serialLotNumber;
    }

    public void setSerialLotNumber(String serialLotNumber) {
        this.serialLotNumber = serialLotNumber;
    }

    public Integer getSerialSold() {
        return serialSold;
    }

    public void setSerialSold(Integer serialSold) {
        this.serialSold = serialSold;
    }

    public Date getLotExpiryDate() {
        return lotExpiryDate;
    }

    public void setLotExpiryDate(Date lotExpiryDate) {
        this.lotExpiryDate = lotExpiryDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logicalRef != null ? logicalRef.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventoryNumber)) {
            return false;
        }
        InventoryNumber other = (InventoryNumber) object;
        if ((this.logicalRef == null && other.logicalRef != null) || (this.logicalRef != null && !this.logicalRef.equals(other.logicalRef))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.PosInventoryNumbers[ logicalRef=" + logicalRef + " ]";
    }
    
}
