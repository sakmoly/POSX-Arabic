/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_invoice")
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "invc_sid")
    private Long invcSid;
    @Basic(optional = false)
    @Column(name = "sbs_no")
    private int sbsNo;
    @Column(name = "invc_no")
    private Integer invcNo;
    @Column(name = "invc_type")
    private Integer invcType;
    @Column(name = "proc_status")
    private Integer procStatus;
    @Column(name = "station")
    private String station;
    @Column(name = "note")
    private String note;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "disc_perc")
    private BigDecimal discPerc;
    @Column(name = "disc_amt")
    private BigDecimal discAmt;
    @Column(name = "disc_perc_spread")
    private BigDecimal discPercSpread;
    @Column(name = "rounding_offset")
    private BigDecimal roundingOffset;
    @Basic(optional = false)
    @Column(name = "business_day")
    @Temporal(TemporalType.TIMESTAMP)
    private Date businessDay;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "post_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;
    @Column(name = "held")
    private Boolean held;
    @Basic(optional = false)
    @Column(name = "time_taken")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeTaken;
    @Column(name = "comment1")
    private String comment1;
    @Column(name = "comment2")
    private String comment2;
    @Column(name = "total_tax_amount")
    private BigDecimal totalTaxAmount;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;
    @JoinColumn(name = "store_no", referencedColumnName = "store_logref")
    @ManyToOne
    private Store storeNo;
    @JoinColumn(name = "cust_sid", referencedColumnName = "cust_sid")
    @ManyToOne
    private Customer custSid;
    //@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    private List<InvoiceItem> invoiceItemsList;
    //@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invcSid")
    private List<InvoiceTender> invoiceTenderList;
    @OneToMany(mappedBy = "openInvcSid")
    private List<Zout> zoutList;
    @OneToMany(mappedBy = "closeInvcSid")
    private List<Zout> zoutList1;

    public Invoice() {
    }

    public Invoice(Long invcSid) {
        this.invcSid = invcSid;
    }

    public Invoice(Long invcSid, int sbsNo, Date businessDay, Date createdDate, Date modifiedDate, Date timeTaken) {
        this.invcSid = invcSid;
        this.sbsNo = sbsNo;
        this.businessDay = businessDay;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.timeTaken = timeTaken;
    }

    public Long getInvcSid() {
        return invcSid;
    }

    public void setInvcSid(Long invcSid) {
        this.invcSid = invcSid;
    }

    public int getSbsNo() {
        return sbsNo;
    }

    public void setSbsNo(int sbsNo) {
        this.sbsNo = sbsNo;
    }

    public Integer getInvcNo() {
        return invcNo;
    }

    public void setInvcNo(Integer invcNo) {
        this.invcNo = invcNo;
    }

    public Integer getInvcType() {
        return invcType;
    }

    public void setInvcType(Integer invcType) {
        this.invcType = invcType;
    }

    public Integer getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(Integer procStatus) {
        this.procStatus = procStatus;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getDiscPerc() {
        return discPerc;
    }

    public void setDiscPerc(BigDecimal discPerc) {
        this.discPerc = discPerc;
    }

    public BigDecimal getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(BigDecimal discAmt) {
        this.discAmt = discAmt;
    }

    public BigDecimal getDiscPercSpread() {
        return discPercSpread;
    }

    public void setDiscPercSpread(BigDecimal discPercSpread) {
        this.discPercSpread = discPercSpread;
    }

    public BigDecimal getRoundingOffset() {
        return roundingOffset;
    }

    public void setRoundingOffset(BigDecimal roundingOffset) {
        this.roundingOffset = roundingOffset;
    }

    public Date getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(Date businessDay) {
        this.businessDay = businessDay;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Boolean getHeld() {
        return held;
    }

    public void setHeld(Boolean held) {
        this.held = held;
    }

    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Store getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(Store storeNo) {
        this.storeNo = storeNo;
    }

    public Customer getCustSid() {
        return custSid;
    }

    public void setCustSid(Customer custSid) {
        this.custSid = custSid;
    }

    public List<InvoiceItem> getInvoiceItemsList() {
        return invoiceItemsList;
    }

    public void setInvoiceItemsList(List<InvoiceItem> invoiceItemsList) {
        this.invoiceItemsList = invoiceItemsList;
    }

    public List<InvoiceTender> getInvoiceTenderList() {
        return invoiceTenderList;
    }

    public void setInvoiceTenderList(List<InvoiceTender> invoiceTenderList) {
        this.invoiceTenderList = invoiceTenderList;
    }

    public List<Zout> getZoutList() {
        return zoutList;
    }

    public void setZoutList(List<Zout> zoutList) {
        this.zoutList = zoutList;
    }

    public List<Zout> getZoutList1() {
        return zoutList1;
    }

    public void setZoutList1(List<Zout> zoutList1) {
        this.zoutList1 = zoutList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invcSid != null ? invcSid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.invcSid == null && other.invcSid != null) || (this.invcSid != null && !this.invcSid.equals(other.invcSid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Invoice[ invcSid=" + invcSid + " ]";
    }
    
}
