/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "pos_credit_card_range")
public class CreditCardRange implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "card_range_id")
    private Integer cardRangeId;
    @Basic(optional = false)
    @Column(name = "lower_range")
    private String lowerRange;
    @Basic(optional = false)
    @Column(name = "upper_range")
    private String upperRange;
    @JoinColumn(name = "card_id", referencedColumnName = "card_id")
    @ManyToOne(optional = false)
    private CreditCard cardId;

    public CreditCardRange() {
    }

    public CreditCardRange(Integer cardRangeId) {
        this.cardRangeId = cardRangeId;
    }

    public CreditCardRange(Integer cardRangeId, String lowerRange, String upperRange) {
        this.cardRangeId = cardRangeId;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }

    public Integer getCardRangeId() {
        return cardRangeId;
    }

    public void setCardRangeId(Integer cardRangeId) {
        this.cardRangeId = cardRangeId;
    }

    public String getLowerRange() {
        return lowerRange;
    }
    
    public BigInteger getIntegerLowerRange() {
        return new BigInteger(lowerRange);
    }

    public void setLowerRange(String lowerRange) {
        this.lowerRange = lowerRange;
    }

    public String getUpperRange() {
        return upperRange;
    }
    
    public BigInteger getIntegerUpperRange() {
        return new BigInteger(upperRange);
    }

    public void setUpperRange(String upperRange) {
        this.upperRange = upperRange;
    }    
    
    public boolean isInRange(BigInteger number) {
//        System.out.println(
//               ((number.compareTo(getIntegerLowerRange()) >= 0) && (number.compareTo(getIntegerUpperRange()) <= 0)) +
//               " : " + getIntegerLowerRange() + " <= " + number + " <= " + getIntegerUpperRange());
        
        return (number.compareTo(getIntegerLowerRange()) >= 0) &&
               (number.compareTo(getIntegerUpperRange()) <= 0) ;
    }

    public CreditCard getCardId() {
        return cardId;
    }

    public void setCardId(CreditCard cardId) {
        this.cardId = cardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardRangeId != null ? cardRangeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCardRange)) {
            return false;
        }
        CreditCardRange other = (CreditCardRange) object;
        if ((this.cardRangeId == null && other.cardRangeId != null) || (this.cardRangeId != null && !this.cardRangeId.equals(other.cardRangeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.CreditCardRange[ cardRangeId=" + cardRangeId + " ]";
    }
    
}
