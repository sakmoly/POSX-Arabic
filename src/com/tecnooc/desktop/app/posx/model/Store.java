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

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_store")
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "sbs_no")
    private int sbsNo;
    @Id
    @Basic(optional = false)
    @Column(name = "store_logref")
    private Integer storeLogref;
    @Basic(optional = false)
    @Column(name = "store_code")
    private String storeCode;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "address3")
    private String address3;
    @Column(name = "address4")
    private String address4;
    @Column(name = "address5")
    private String address5;
    @Column(name = "zip")
    private String zip;
    @Column(name = "phone1")
    private String phone1;
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "num_pos")
    private Integer numPos;
    @Column(name = "active")
    private Boolean active;
    @OneToMany(mappedBy = "storeNo")
    private List<Invoice> invoiceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storeLogref")
    private List<Terminal> terminalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storeLogref")
    private List<DocSequence> docSequenceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storeNo")
    private List<Zout> zoutList;

    public Store() {
    }

    public Store(Integer storeLogref) {
        this.storeLogref = storeLogref;
    }

    public Store(Integer storeLogref, int sbsNo, String storeCode) {
        this.storeLogref = storeLogref;
        this.sbsNo = sbsNo;
        this.storeCode = storeCode;
    }

    public int getSbsNo() {
        return sbsNo;
    }

    public void setSbsNo(int sbsNo) {
        this.sbsNo = sbsNo;
    }

    public Integer getStoreLogref() {
        return storeLogref;
    }

    public void setStoreLogref(Integer storeLogref) {
        this.storeLogref = storeLogref;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public Integer getNumPos() {
        return numPos;
    }

    public void setNumPos(Integer numPos) {
        this.numPos = numPos;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public List<Terminal> getTerminalList() {
        return terminalList;
    }

    public void setTerminalList(List<Terminal> terminalList) {
        this.terminalList = terminalList;
    }

    public List<DocSequence> getDocSequenceList() {
        return docSequenceList;
    }

    public void setDocSequenceList(List<DocSequence> docSequenceList) {
        this.docSequenceList = docSequenceList;
    }

    public List<Zout> getZoutList() {
        return zoutList;
    }

    public void setZoutList(List<Zout> zoutList) {
        this.zoutList = zoutList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storeLogref != null ? storeLogref.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Store)) {
            return false;
        }
        Store other = (Store) object;
        if ((this.storeLogref == null && other.storeLogref != null) || (this.storeLogref != null && !this.storeLogref.equals(other.storeLogref))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Store[ storeLogref=" + storeLogref + " ]";
    }
    
}
