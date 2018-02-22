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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_card")
public class CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "card_id")
    private Integer cardId;
    @Column(name = "card_name")
    private String cardName;
    @Basic(optional = false)
    @Column(name = "card_short_name")
    private String cardShortName;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId")
    private List<CreditCardRange> creditCardRangeList;

    public CreditCard() {
    }

    public CreditCard(Integer cardId) {
        this.cardId = cardId;
    }

    public CreditCard(Integer cardId, String cardShortName, boolean active) {
        this.cardId = cardId;
        this.cardShortName = cardShortName;
        this.active = active;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardShortName() {
        return cardShortName;
    }

    public void setCardShortName(String cardShortName) {
        this.cardShortName = cardShortName;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CreditCardRange> getCreditCardRangeList() {
        return creditCardRangeList;
    }

    public void setCreditCardRangeList(List<CreditCardRange> creditCardRangeList) {
        this.creditCardRangeList = creditCardRangeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardId != null ? cardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.cardId == null && other.cardId != null) || (this.cardId != null && !this.cardId.equals(other.cardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Card[ cardId=" + cardId + " ]";
    }
    
}
