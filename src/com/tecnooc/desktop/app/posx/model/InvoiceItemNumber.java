/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
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
@Table(name = "pos_invoice_item_numbers")
public class InvoiceItemNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "item_sid")
    private Long itemSid;
    @Column(name = "invc_sid")
    private Long invcSid;
    @Column(name = "inventory_number_ref")
    private Long inventoryNumberRef;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemSid() {
        return itemSid;
    }

    public void setItemSid(Long itemSid) {
        this.itemSid = itemSid;
    }

    public Long getInvcSid() {
        return invcSid;
    }

    public void setInvcSid(Long invcSid) {
        this.invcSid = invcSid;
    }

    public Long getInventoryNumberRef() {
        return inventoryNumberRef;
    }

    public void setInventoryNumberRef(Long inventoryNumberRef) {
        this.inventoryNumberRef = inventoryNumberRef;
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
        if (!(object instanceof InvoiceItemNumber)) {
            return false;
        }
        InvoiceItemNumber other = (InvoiceItemNumber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.InvoiceItemNumber[ id=" + id + " ]";
    }
    
}
