/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "pos_inventory")
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "item_sid")
    private Long itemSid;
    @Basic(optional = false)
    @Column(name = "upc")
    private String upc;
    @Column(name = "use_qty_decimals")
    private Integer useQtyDecimals;
    @Basic(optional = false)
    @Column(name = "sbs_no")
    private int sbsNo;
    @Column(name = "alu")
    private String alu;
    @Basic(optional = false)
    @Column(name = "dcs_code")
    private String dcsCode;
    @Column(name = "vend_code")
    private String vendCode;
    @Column(name = "description1")
    private String description1;
    @Column(name = "description2")
    private String description2;
    @Column(name = "description3")
    private String description3;
    @Column(name = "description4")
    private String description4;
    @Column(name = "attr")
    private String attr;
    @Column(name = "size")
    private String size;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "max_disc_perc1")
    private BigDecimal maxDiscPerc1;
    @Column(name = "udf1_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date udf1Date;
    @Column(name = "udf2_value")
    private String udf2Value;
    @Column(name = "item_no")
    private Integer itemNo;
    @Basic(optional = false)
    @Column(name = "cms_post_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cmsPostDate;
    @Column(name = "eci_flag")
    private Boolean eciFlag;
    @Column(name = "qty")
    private BigDecimal qty;
    @Column(name = "active")
    private Boolean active;
    @Basic(optional = false)
    @Column(name = "base_price")
    private BigDecimal basePrice;
    @Basic(optional = false)
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;
    @Column(name = "unit")
    private String unit;
    @Column(name = "unit_per_case")
    private Integer unitPerCase;
    @Column(name = "weighing_item")
    private Boolean weighingItem;
    @Column(name = "tax_type")
    private String taxType;
    @Column(name = "tax_rate")
    private BigDecimal taxRate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
    private List<InvoiceItem> invoiceItemsList;

    public Inventory() {
    }

    public Inventory(Long itemSid) {
        this.itemSid = itemSid;
    }

    public Inventory(Long itemSid, String upc, int sbsNo, String dcsCode, Date cmsPostDate, BigDecimal basePrice, BigDecimal sellingPrice) {
        this.itemSid = itemSid;
        this.upc = upc;
        this.sbsNo = sbsNo;
        this.dcsCode = dcsCode;
        this.cmsPostDate = cmsPostDate;
        this.basePrice = basePrice;
        this.sellingPrice = sellingPrice;
    }

    public Long getItemSid() {
        return itemSid;
    }

    public void setItemSid(Long itemSid) {
        this.itemSid = itemSid;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public int getUseQtyDecimals() {
        return useQtyDecimals == null ? 0 : useQtyDecimals;
    }

    public void setUseQtyDecimals(int useQtyDecimals) {
        this.useQtyDecimals = useQtyDecimals;
    }

    public int getSbsNo() {
        return sbsNo;
    }

    public void setSbsNo(int sbsNo) {
        this.sbsNo = sbsNo;
    }

    public String getAlu() {
        return alu;
    }

    public void setAlu(String alu) {
        this.alu = alu;
    }

    public String getDcsCode() {
        return dcsCode;
    }

    public void setDcsCode(String dcsCode) {
        this.dcsCode = dcsCode;
    }

    public String getVendCode() {
        return vendCode;
    }

    public void setVendCode(String vendCode) {
        this.vendCode = vendCode;
    }

    public String getDescription1() {
        return description1 == null ? "" : description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2 == null ? "" : description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3 == null ? "" : description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getDescription4() {
        return description4 == null ? "" : description4;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getCost() {
        return cost == null ? BigDecimal.ZERO : cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getMaxDiscPerc1() {
        return maxDiscPerc1 == null ? BigDecimal.ZERO : maxDiscPerc1;
    }

    public void setMaxDiscPerc1(BigDecimal maxDiscPerc1) {
        this.maxDiscPerc1 = maxDiscPerc1;
    }

    public Date getUdf1Date() {
        return udf1Date;
    }

    public void setUdf1Date(Date udf1Date) {
        this.udf1Date = udf1Date;
    }

    public String getUdf2Value() {
        return udf2Value;
    }

    public void setUdf2Value(String udf2Value) {
        this.udf2Value = udf2Value;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public Date getCmsPostDate() {
        return cmsPostDate;
    }

    public void setCmsPostDate(Date cmsPostDate) {
        this.cmsPostDate = cmsPostDate;
    }

    public Boolean getEciFlag() {
        return eciFlag;
    }

    public void setEciFlag(Boolean eciFlag) {
        this.eciFlag = eciFlag;
    }

    public BigDecimal getQty() {
        return qty == null ? BigDecimal.ZERO : qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getUnitPerCase() {
        return unitPerCase;
    }

    public void setUnitPerCase(Integer unitPerCase) {
        this.unitPerCase = unitPerCase;
    }

    public Boolean getWeighingItem() {
        return weighingItem;
    }

    public void setWeighingItem(Boolean weighingItem) {
        this.weighingItem = weighingItem;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
    public List<InvoiceItem> getInvoiceItemsList() {
        return invoiceItemsList;
    }

    public void setInvoiceItemsList(List<InvoiceItem> invoiceItemsList) {
        this.invoiceItemsList = invoiceItemsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemSid != null ? itemSid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventory)) {
            return false;
        }
        Inventory other = (Inventory) object;
        if ((this.itemSid == null && other.itemSid != null) || (this.itemSid != null && !this.itemSid.equals(other.itemSid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Inventory[ itemSid=" + itemSid + " ]";
    }
    
}
