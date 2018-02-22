/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_currency_denomination")
public class CurrencyDenomination implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "currency_denom_id")
    private Integer currencyDenomId;
    @Basic(optional = false)
    @Column(name = "currency_denom_name")
    private String currencyDenomName;
    @Basic(optional = false)
    @Column(name = "multiplier")
    private BigDecimal multiplier;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currencyDenomination")
    private List<ZoutCurrency> zoutCurrencyList;
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    @ManyToOne(optional = false)
    private Currency currencyId;

    public CurrencyDenomination() {
    }

    public CurrencyDenomination(Integer currencyDenomId) {
        this.currencyDenomId = currencyDenomId;
    }

    public CurrencyDenomination(Integer currencyDenomId, String currencyDenomName, BigDecimal multiplier, boolean active) {
        this.currencyDenomId = currencyDenomId;
        this.currencyDenomName = currencyDenomName;
        this.multiplier = multiplier;
        this.active = active;
    }

    public Integer getCurrencyDenomId() {
        return currencyDenomId;
    }

    public void setCurrencyDenomId(Integer currencyDenomId) {
        this.currencyDenomId = currencyDenomId;
    }

    public String getCurrencyDenomName() {
        return currencyDenomName;
    }

    public void setCurrencyDenomName(String currencyDenomName) {
        this.currencyDenomName = currencyDenomName;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<ZoutCurrency> getZoutCurrencyList() {
        return zoutCurrencyList;
    }

    public void setZoutCurrencyList(List<ZoutCurrency> zoutCurrencyList) {
        this.zoutCurrencyList = zoutCurrencyList;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (currencyDenomId != null ? currencyDenomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CurrencyDenomination)) {
            return false;
        }
        CurrencyDenomination other = (CurrencyDenomination) object;
        if ((this.currencyDenomId == null && other.currencyDenomId != null) || (this.currencyDenomId != null && !this.currencyDenomId.equals(other.currencyDenomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.CurrencyDenomination[ currencyDenomId=" + currencyDenomId + " ]";
    }
    
}
