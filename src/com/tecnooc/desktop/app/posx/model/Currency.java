/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_currency")
public class Currency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "currency_id")
    private Integer currencyId;
    @Basic(optional = false)
    @Column(name = "currency_abbr")
    private String currencyAbbr;
    @Basic(optional = false)
    @Column(name = "currency_name")
    private String currencyName;
    @Basic(optional = false)
    @Column(name = "rounding")
    private int rounding;
    @Basic(optional = false)
    @Column(name = "decimals")
    private int decimals;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currencyId")
    private List<CurrencyDenomination> currencyDenominationList;

    public Currency() {
    }

    public Currency(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Currency(Integer currencyId, String currencyAbbr, String currencyName, int rounding, int decimals, boolean active) {
        this.currencyId = currencyId;
        this.currencyAbbr = currencyAbbr;
        this.currencyName = currencyName;
        this.rounding = rounding;
        this.decimals = decimals;
        this.active = active;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getRounding() {
        return rounding;
    }

    public void setRounding(int rounding) {
        this.rounding = rounding;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CurrencyDenomination> getCurrencyDenominationList() {
        return currencyDenominationList;
    }

    public void setCurrencyDenominationList(List<CurrencyDenomination> currencyDenominationList) {
        this.currencyDenominationList = currencyDenominationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (currencyId != null ? currencyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.currencyId == null && other.currencyId != null) || (this.currencyId != null && !this.currencyId.equals(other.currencyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Currency[ currencyId=" + currencyId + " ]";
    }
    
}
