/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jomit
 */
@Embeddable
public class InvoiceItemPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "item_sid")
    private long itemSid;
    @Basic(optional = false)
    @Column(name = "invc_sid")
    private long invcSid;

    public InvoiceItemPK() {
    }

    public InvoiceItemPK(long itemSid, long invcSid) {
        this.itemSid = itemSid;
        this.invcSid = invcSid;
    }

    public long getItemSid() {
        return itemSid;
    }

    public void setItemSid(long itemSid) {
        this.itemSid = itemSid;
    }

    public long getInvcSid() {
        return invcSid;
    }

    public void setInvcSid(long invcSid) {
        this.invcSid = invcSid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) itemSid;
        hash += (int) invcSid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceItemPK)) {
            return false;
        }
        InvoiceItemPK other = (InvoiceItemPK) object;
        if (this.itemSid != other.itemSid) {
            return false;
        }
        if (this.invcSid != other.invcSid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.InvoiceItemsPK[ itemSid=" + itemSid + ", invcSid=" + invcSid + " ]";
    }
    
}
