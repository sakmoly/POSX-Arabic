/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_zout_currency")
public class ZoutCurrency implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZoutCurrencyPK zoutCurrencyPK;
    @Basic(optional = false)
    @Column(name = "multiplier")
    private int multiplier;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "actual_amount")
    private BigDecimal actualAmount;
    @Column(name = "amount_diff")
    private BigDecimal amountDiff;
    @JoinColumn(name = "zout_sid", referencedColumnName = "zout_sid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Zout zout;
    @JoinColumn(name = "crrency_denom_id", referencedColumnName = "currency_denom_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CurrencyDenomination currencyDenomination;

    public ZoutCurrency() {
    }

    public ZoutCurrency(ZoutCurrencyPK zoutCurrencyPK) {
        this.zoutCurrencyPK = zoutCurrencyPK;
    }

    public ZoutCurrency(ZoutCurrencyPK zoutCurrencyPK, int multiplier) {
        this.zoutCurrencyPK = zoutCurrencyPK;
        this.multiplier = multiplier;
    }

    public ZoutCurrency(long zoutSid, int crrencyDenomId) {
        this.zoutCurrencyPK = new ZoutCurrencyPK(zoutSid, crrencyDenomId);
    }

    public ZoutCurrencyPK getZoutCurrencyPK() {
        return zoutCurrencyPK;
    }

    public void setZoutCurrencyPK(ZoutCurrencyPK zoutCurrencyPK) {
        this.zoutCurrencyPK = zoutCurrencyPK;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
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

    public Zout getZout() {
        return zout;
    }

    public void setZout(Zout zout) {
        this.zout = zout;
    }

    public CurrencyDenomination getCurrencyDenomination() {
        return currencyDenomination;
    }

    public void setCurrencyDenomination(CurrencyDenomination currencyDenomination) {
        this.currencyDenomination = currencyDenomination;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zoutCurrencyPK != null ? zoutCurrencyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZoutCurrency)) {
            return false;
        }
        ZoutCurrency other = (ZoutCurrency) object;
        if ((this.zoutCurrencyPK == null && other.zoutCurrencyPK != null) || (this.zoutCurrencyPK != null && !this.zoutCurrencyPK.equals(other.zoutCurrencyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.ZoutCurrency[ zoutCurrencyPK=" + zoutCurrencyPK + " ]";
    }
    
}
