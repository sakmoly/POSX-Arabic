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
import javax.persistence.FetchType;
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
@Table(name = "pos_zout")
public class Zout implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "zout_sid")
    private Long zoutSid;
    @Basic(optional = false)
    @Column(name = "sbs_no")
    private int sbsNo;
    @Column(name = "business_day")
    @Temporal(TemporalType.DATE)
    private Date businessDay;
    @Column(name = "period_begin")
    @Temporal(TemporalType.DATE)
    private Date periodBegin;
    @Column(name = "period_end")
    @Temporal(TemporalType.DATE)
    private Date periodEnd;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tender_total_open")
    private BigDecimal tenderTotalOpen;
    @Column(name = "tender_total_close")
    private BigDecimal tenderTotalClose;
    @Column(name = "over_short_amt")
    private BigDecimal overShortAmt;
    @Column(name = "drawer_leave_amt")
    private BigDecimal drawerLeaveAmt;
    @Column(name = "deposit_amt")
    private BigDecimal depositAmt;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "modified_date")
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;
    @Column(name = "retry_count")
    private Integer retryCount;
    @Column(name = "computer_name")
    private String computerName;
    @Column(name = "proc_status")
    private Integer procStatus;
    //@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zout")
    private List<ZoutCurrency> zoutCurrencyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zoutSid")
    private List<ZoutTender> zoutTenderList;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private User userId;
    @JoinColumn(name = "terminal_id", referencedColumnName = "terminal_logref")
    @ManyToOne(optional = false)
    private Terminal terminalId;
    @JoinColumn(name = "store_no", referencedColumnName = "store_logref")
    @ManyToOne(optional = false)
    private Store storeNo;
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "open_invc_sid", referencedColumnName = "invc_sid")
    @ManyToOne
    private Invoice openInvcSid;
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "close_invc_sid", referencedColumnName = "invc_sid")
    @ManyToOne
    private Invoice closeInvcSid;

    public Zout() {
    }

    public Zout(Long zoutSid) {
        this.zoutSid = zoutSid;
    }

    public Zout(Long zoutSid, int sbsNo, Date createdDate, Date modifiedDate) {
        this.zoutSid = zoutSid;
        this.sbsNo = sbsNo;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getZoutSid() {
        return zoutSid;
    }

    public void setZoutSid(Long zoutSid) {
        this.zoutSid = zoutSid;
    }

    public int getSbsNo() {
        return sbsNo;
    }

    public void setSbsNo(int sbsNo) {
        this.sbsNo = sbsNo;
    }

    public Date getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(Date businessDay) {
        this.businessDay = businessDay;
    }

    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public BigDecimal getTenderTotalOpen() {
        return tenderTotalOpen;
    }

    public void setTenderTotalOpen(BigDecimal tenderTotalOpen) {
        this.tenderTotalOpen = tenderTotalOpen;
    }

    public BigDecimal getTenderTotalClose() {
        return tenderTotalClose;
    }

    public void setTenderTotalClose(BigDecimal tenderTotalClose) {
        this.tenderTotalClose = tenderTotalClose;
    }

    public BigDecimal getOverShortAmt() {
        return overShortAmt;
    }

    public void setOverShortAmt(BigDecimal overShortAmt) {
        this.overShortAmt = overShortAmt;
    }

    public BigDecimal getDrawerLeaveAmt() {
        return drawerLeaveAmt;
    }

    public void setDrawerLeaveAmt(BigDecimal drawerLeaveAmt) {
        this.drawerLeaveAmt = drawerLeaveAmt;
    }

    public BigDecimal getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(BigDecimal depositAmt) {
        this.depositAmt = depositAmt;
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

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Integer getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(Integer procStatus) {
        this.procStatus = procStatus;
    }

    public List<ZoutCurrency> getZoutCurrencyList() {
        return zoutCurrencyList;
    }

    public void setZoutCurrencyList(List<ZoutCurrency> zoutCurrencyList) {
        this.zoutCurrencyList = zoutCurrencyList;
    }

    public List<ZoutTender> getZoutTenderList() {
        return zoutTenderList;
    }

    public void setZoutTenderList(List<ZoutTender> zoutTenderList) {
        this.zoutTenderList = zoutTenderList;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Terminal getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Terminal terminalId) {
        this.terminalId = terminalId;
    }

    public Store getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(Store storeNo) {
        this.storeNo = storeNo;
    }

    public Invoice getOpenInvcSid() {
        return openInvcSid;
    }

    public void setOpenInvcSid(Invoice openInvcSid) {
        this.openInvcSid = openInvcSid;
    }

    public Invoice getCloseInvcSid() {
        return closeInvcSid;
    }

    public void setCloseInvcSid(Invoice closeInvcSid) {
        this.closeInvcSid = closeInvcSid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zoutSid != null ? zoutSid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zout)) {
            return false;
        }
        Zout other = (Zout) object;
        if ((this.zoutSid == null && other.zoutSid != null) || (this.zoutSid != null && !this.zoutSid.equals(other.zoutSid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Zout[ zoutSid=" + zoutSid + " ]";
    }
    
}
