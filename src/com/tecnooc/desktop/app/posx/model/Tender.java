/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_tender")
public class Tender implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "tender_id")
    private Integer tenderId;
    @Basic(optional = false)
    @Column(name = "tender_type")
    private int tenderType;
    @Basic(optional = false)
    @Column(name = "tender_name")
    private String tenderName;
    @Basic(optional = false)
    @Column(name = "cust_req_tender")
    private boolean custReqTender;
    @Basic(optional = false)
    @Column(name = "cust_req_change")
    private boolean custReqChange;
    @Basic(optional = false)
    @Column(name = "xz_include")
    private boolean xzInclude;
    @Basic(optional = false)
    @Column(name = "media_count_include")
    private boolean mediaCountInclude;
    @Basic(optional = false)
    @Column(name = "return_invoice_include")
    private boolean returnInvoiceInclude;
    @Basic(optional = false)
    @Column(name = "tender_short_lbl")
    private String tenderShortLbl;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;

    public Tender() {
    }

    public Tender(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public Tender(Integer tenderId, int tenderType, String tenderName, boolean custReqTender, boolean custReqChange, boolean xzInclude, boolean mediaCountInclude, boolean returnInvoiceInclude, String tenderShortLbl, boolean active) {
        this.tenderId = tenderId;
        this.tenderType = tenderType;
        this.tenderName = tenderName;
        this.custReqTender = custReqTender;
        this.custReqChange = custReqChange;
        this.xzInclude = xzInclude;
        this.mediaCountInclude = mediaCountInclude;
        this.returnInvoiceInclude = returnInvoiceInclude;
        this.tenderShortLbl = tenderShortLbl;
        this.active = active;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public int getTenderType() {
        return tenderType;
    }

    public void setTenderType(int tenderType) {
        this.tenderType = tenderType;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public boolean getCustReqTender() {
        return custReqTender;
    }

    public void setCustReqTender(boolean custReqTender) {
        this.custReqTender = custReqTender;
    }

    public boolean getCustReqChange() {
        return custReqChange;
    }

    public void setCustReqChange(boolean custReqChange) {
        this.custReqChange = custReqChange;
    }

    public boolean getXzInclude() {
        return xzInclude;
    }

    public void setXzInclude(boolean xzInclude) {
        this.xzInclude = xzInclude;
    }

    public boolean getMediaCountInclude() {
        return mediaCountInclude;
    }

    public void setMediaCountInclude(boolean mediaCountInclude) {
        this.mediaCountInclude = mediaCountInclude;
    }

    public boolean getReturnInvoiceInclude() {
        return returnInvoiceInclude;
    }

    public void setReturnInvoiceInclude(boolean returnInvoiceInclude) {
        this.returnInvoiceInclude = returnInvoiceInclude;
    }

    public String getTenderShortLbl() {
        return tenderShortLbl;
    }

    public void setTenderShortLbl(String tenderShortLbl) {
        this.tenderShortLbl = tenderShortLbl;
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
        hash += (tenderId != null ? tenderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tender)) {
            return false;
        }
        Tender other = (Tender) object;
        if ((this.tenderId == null && other.tenderId != null) || (this.tenderId != null && !this.tenderId.equals(other.tenderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Tender[ tenderId=" + tenderId + " ]";
    }
    
}
