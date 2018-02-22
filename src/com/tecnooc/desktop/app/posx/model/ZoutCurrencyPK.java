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
public class ZoutCurrencyPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "zout_sid")
    private long zoutSid;
    @Basic(optional = false)
    @Column(name = "crrency_denom_id")
    private int crrencyDenomId;

    public ZoutCurrencyPK() {
    }

    public ZoutCurrencyPK(long zoutSid, int crrencyDenomId) {
        this.zoutSid = zoutSid;
        this.crrencyDenomId = crrencyDenomId;
    }

    public long getZoutSid() {
        return zoutSid;
    }

    public void setZoutSid(long zoutSid) {
        this.zoutSid = zoutSid;
    }

    public int getCrrencyDenomId() {
        return crrencyDenomId;
    }

    public void setCrrencyDenomId(int crrencyDenomId) {
        this.crrencyDenomId = crrencyDenomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) zoutSid;
        hash += (int) crrencyDenomId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZoutCurrencyPK)) {
            return false;
        }
        ZoutCurrencyPK other = (ZoutCurrencyPK) object;
        if (this.zoutSid != other.zoutSid) {
            return false;
        }
        if (this.crrencyDenomId != other.crrencyDenomId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.ZoutCurrencyPK[ zoutSid=" + zoutSid + ", crrencyDenomId=" + crrencyDenomId + " ]";
    }
    
}
