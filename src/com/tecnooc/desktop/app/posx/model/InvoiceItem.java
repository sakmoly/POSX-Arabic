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
@Table(name = "pos_invoice_items")
public class InvoiceItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvoiceItemPK invoiceItemsPK;
    @Basic(optional = false)
    @Column(name = "transaction_type")
    private int transactionType;
    @Basic(optional = false)
    @Column(name = "qty")
    private BigDecimal qty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "cost")
    private BigDecimal cost;
    @Basic(optional = false)
    @Column(name = "base_price")
    private BigDecimal basePrice;
    @Basic(optional = false)
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;
    @Column(name = "disc_amount")
    private BigDecimal discAmount;
    @Column(name = "disc_perc")
    private BigDecimal discPerc;
    @Column(name = "doc_disc_amount")
    private BigDecimal docDiscAmount;
    @Column(name = "doc_disc_perc_doc")
    private BigDecimal docDiscPercDoc;
    @Column(name = "tax_rate")
    private BigDecimal taxRate;
    @Column(name = "tax_amount")
    private BigDecimal taxAmount;
    @Column(name = "tax_type")
    private String taxType;
    @JoinColumn(name = "item_sid", referencedColumnName = "item_sid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Inventory inventory;
    @JoinColumn(name = "invc_sid", referencedColumnName = "invc_sid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Invoice invoice;

    public InvoiceItem() {
    }

    public InvoiceItem(InvoiceItemPK invoiceItemsPK) {
        this.invoiceItemsPK = invoiceItemsPK;
    }

    public InvoiceItem(InvoiceItemPK invoiceItemsPK, int transactionType, BigDecimal qty, BigDecimal cost, BigDecimal basePrice, BigDecimal sellingPrice) {
        this.invoiceItemsPK = invoiceItemsPK;
        this.transactionType = transactionType;
        this.qty = qty;
        this.cost = cost;
        this.basePrice = basePrice;
        this.sellingPrice = sellingPrice;
    }

    public InvoiceItem(long itemSid, long invcSid) {
        this.invoiceItemsPK = new InvoiceItemPK(itemSid, invcSid);
    }

    public InvoiceItemPK getInvoiceItemsPK() {
        return invoiceItemsPK;
    }

    public void setInvoiceItemsPK(InvoiceItemPK invoiceItemsPK) {
        this.invoiceItemsPK = invoiceItemsPK;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getDiscAmount() {
        return discAmount;
    }

    public void setDiscAmount(BigDecimal discAmount) {
        this.discAmount = discAmount;
    }

    public BigDecimal getDiscPerc() {
        return discPerc;
    }

    public void setDiscPerc(BigDecimal discPerc) {
        this.discPerc = discPerc;
    }

    public BigDecimal getDocDiscAmount() {
        return docDiscAmount;
    }

    public void setDocDiscAmount(BigDecimal docDiscAmount) {
        this.docDiscAmount = docDiscAmount;
    }

    public BigDecimal getDocDiscPercDoc() {
        return docDiscPercDoc;
    }

    public void setDocDiscPercDoc(BigDecimal docDiscPercDoc) {
        this.docDiscPercDoc = docDiscPercDoc;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceItemsPK != null ? invoiceItemsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceItem)) {
            return false;
        }
        InvoiceItem other = (InvoiceItem) object;
        if ((this.invoiceItemsPK == null && other.invoiceItemsPK != null) || (this.invoiceItemsPK != null && !this.invoiceItemsPK.equals(other.invoiceItemsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.InvoiceItems[ invoiceItemsPK=" + invoiceItemsPK + " ]";
    }
    
}
