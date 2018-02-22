/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_zout_tender")
public class ZoutTender implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tender_id")
    private Integer tenderId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "actual_amount")
    private BigDecimal actualAmount;
    @Column(name = "amount_diff")
    private BigDecimal amountDiff;
    @JoinColumn(name = "zout_sid", referencedColumnName = "zout_sid")
    @ManyToOne(optional = false)
    private Zout zoutSid;

    public ZoutTender() {
    }

    public ZoutTender(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getAmountDiff() {
        return amountDiff;
    }

    public void setAmountDiff(BigDecimal amountDiff) {
        this.amountDiff = amountDiff;
    }

    public Zout getZoutSid() {
        return zoutSid;
    }

    public void setZoutSid(Zout zoutSid) {
        this.zoutSid = zoutSid;
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
        if (!(object instanceof ZoutTender)) {
            return false;
        }
        ZoutTender other = (ZoutTender) object;
        if ((this.tenderId == null && other.tenderId != null) || (this.tenderId != null && !this.tenderId.equals(other.tenderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.ZoutTender[ tenderId=" + tenderId + " ]";
    }
    
}
