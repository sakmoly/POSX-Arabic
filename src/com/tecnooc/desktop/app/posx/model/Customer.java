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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cust_sid")
    private Long custSid;
    @Column(name = "cust_id")
    private String custId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "mob")
    private String mob;
    @Column(name = "telephone1")
    private String telephone1;
    @Column(name = "telephone2")
    private String telephone2;
    @Column(name = "credit_limit")
    private Integer creditLimit;
    @Column(name = "credit_used")
    private Integer creditUsed;
    @Column(name = "store_credit")
    private Integer storeCredit;
    @Column(name = "max_disc_perc")
    private Integer maxDiscPerc;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "udf1_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date udf1Date;
    @Column(name = "udf2_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date udf2Date;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "email_addr")
    private String emailAddr;
    @Column(name = "cust_type")
    private Integer custType;
    @Column(name = "notes")
    private String notes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "disc_perc_upper_limit")
    private BigDecimal discPercUpperLimit;
    @Column(name = "term_type")
    private Integer termType;
    @Column(name = "cust_class_name")
    private String custClassName;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "custSid")
    private List<Invoice> invoiceList;

    public Customer() {
    }

    public Customer(Long custSid) {
        this.custSid = custSid;
    }

    public Customer(Long custSid, Date createdDate, Date modifiedDate) {
        this.custSid = custSid;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getCustSid() {
        return custSid;
    }

    public void setCustSid(Long custSid) {
        this.custSid = custSid;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public Integer getCreditLimit() {
        return creditLimit == null ? 0 : creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getCreditUsed() {
        return creditUsed == null ? 0 : creditUsed;
    }

    public void setCreditUsed(Integer creditUsed) {
        this.creditUsed = creditUsed;
    }

    public Integer getStoreCredit() {
        return storeCredit == null ? 0 : storeCredit;
    }

    public void setStoreCredit(Integer storeCredit) {
        this.storeCredit = storeCredit;
    }

    public Integer getMaxDiscPerc() {
        return maxDiscPerc == null ? 0 : maxDiscPerc;
    }

    public void setMaxDiscPerc(Integer maxDiscPerc) {
        this.maxDiscPerc = maxDiscPerc;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getUdf1Date() {
        return udf1Date;
    }

    public void setUdf1Date(Date udf1Date) {
        this.udf1Date = udf1Date;
    }

    public Date getUdf2Date() {
        return udf2Date;
    }

    public void setUdf2Date(Date udf2Date) {
        this.udf2Date = udf2Date;
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

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public Integer getCustType() {
        return custType;
    }

    public void setCustType(Integer custType) {
        this.custType = custType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getDiscPercUpperLimit() {
        return discPercUpperLimit;
    }

    public void setDiscPercUpperLimit(BigDecimal discPercUpperLimit) {
        this.discPercUpperLimit = discPercUpperLimit;
    }

    public Integer getTermType() {
        return termType;
    }

    public void setTermType(Integer termType) {
        this.termType = termType;
    }

    public String getCustClassName() {
        return custClassName;
    }

    public void setCustClassName(String custClassName) {
        this.custClassName = custClassName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (custSid != null ? custSid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.custSid == null && other.custSid != null) || (this.custSid != null && !this.custSid.equals(other.custSid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Customer[ custSid=" + custSid + " ]";
    }
    
}
