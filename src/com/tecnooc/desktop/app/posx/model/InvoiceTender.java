/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Table(name = "pos_invoice_tender")
public class InvoiceTender implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tender_no")
    private Integer tenderNo;
    @Basic(optional = false)
    @Column(name = "tender_type")
    private int tenderType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taken")
    private BigDecimal taken;
    @Column(name = "given")
    private BigDecimal given;
    @Column(name = "amt")
    private BigDecimal amt;
    @Column(name = "auth")
    private String auth;
    @Column(name = "reference")
    private String reference;
    @Column(name = "crd_type")
    private Integer crdType;
    @Column(name = "crd_exp_month")
    private Integer crdExpMonth;
    @Column(name = "crd_exp_year")
    private Integer crdExpYear;
    @Column(name = "crd_normal_sale")
    private Boolean crdNormalSale;
    @Column(name = "crd_contr_no")
    private String crdContrNo;
    @Column(name = "manual_remark")
    private String manualRemark;
    @Column(name = "cashback_amt")
    private BigDecimal cashbackAmt;
    @Column(name = "orig_crd_name")
    private String origCrdName;
    @Column(name = "orig_currency_name")
    private String origCurrencyName;
    @Column(name = "cardholder_name")
    private String cardholderName;
    @Column(name = "crd_name")
    private String crdName;
    @Column(name = "currency_name")
    private String currencyName;
    @JoinColumn(name = "invc_sid", referencedColumnName = "invc_sid")
    @ManyToOne(optional = false)
    private Invoice invcSid;

    public InvoiceTender() {
    }

    public InvoiceTender(Integer tenderNo) {
        this.tenderNo = tenderNo;
    }

    public InvoiceTender(Integer tenderNo, int tenderType) {
        this.tenderNo = tenderNo;
        this.tenderType = tenderType;
    }

    public Integer getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(Integer tenderNo) {
        this.tenderNo = tenderNo;
    }

    public int getTenderType() {
        return tenderType;
    }

    public void setTenderType(int tenderType) {
        this.tenderType = tenderType;
    }

    public BigDecimal getTaken() {
        return taken;
    }

    public void setTaken(BigDecimal taken) {
        this.taken = taken;
    }

    public BigDecimal getGiven() {
        return given;
    }

    public void setGiven(BigDecimal given) {
        this.given = given;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getCrdType() {
        return crdType;
    }

    public void setCrdType(Integer crdType) {
        this.crdType = crdType;
    }

    public Integer getCrdExpMonth() {
        return crdExpMonth;
    }

    public void setCrdExpMonth(Integer crdExpMonth) {
        this.crdExpMonth = crdExpMonth;
    }

    public Integer getCrdExpYear() {
        return crdExpYear;
    }

    public void setCrdExpYear(Integer crdExpYear) {
        this.crdExpYear = crdExpYear;
    }

    public Boolean getCrdNormalSale() {
        return crdNormalSale;
    }

    public void setCrdNormalSale(Boolean crdNormalSale) {
        this.crdNormalSale = crdNormalSale;
    }

    public String getCrdContrNo() {
        return crdContrNo;
    }

    public void setCrdContrNo(String crdContrNo) {
        this.crdContrNo = crdContrNo;
    }

    public String getManualRemark() {
        return manualRemark;
    }

    public void setManualRemark(String manualRemark) {
        this.manualRemark = manualRemark;
    }

    public BigDecimal getCashbackAmt() {
        return cashbackAmt;
    }

    public void setCashbackAmt(BigDecimal cashbackAmt) {
        this.cashbackAmt = cashbackAmt;
    }

    public String getOrigCrdName() {
        return origCrdName;
    }

    public void setOrigCrdName(String origCrdName) {
        this.origCrdName = origCrdName;
    }

    public String getOrigCurrencyName() {
        return origCurrencyName;
    }

    public void setOrigCurrencyName(String origCurrencyName) {
        this.origCurrencyName = origCurrencyName;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCrdName() {
        return crdName;
    }

    public void setCrdName(String crdName) {
        this.crdName = crdName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Invoice getInvcSid() {
        return invcSid;
    }

    public void setInvcSid(Invoice invcSid) {
        this.invcSid = invcSid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tenderNo != null ? tenderNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceTender)) {
            return false;
        }
        InvoiceTender other = (InvoiceTender) object;
        if ((this.tenderNo == null && other.tenderNo != null) || (this.tenderNo != null && !this.tenderNo.equals(other.tenderNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.InvoiceTender[ tenderNo=" + tenderNo + " ]";
    }
    
}
