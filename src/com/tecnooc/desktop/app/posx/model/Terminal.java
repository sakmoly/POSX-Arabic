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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_terminal")
public class Terminal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "terminal_logref")
    private Integer terminalLogref;
    @Basic(optional = false)
    @Column(name = "terminal_code")
    private String terminalCode;
    @Basic(optional = false)
    @Column(name = "terminal_name")
    private String terminalName;
    @Column(name = "active")
    private Boolean active;
    @JoinColumn(name = "store_logref", referencedColumnName = "store_logref")
    @ManyToOne(optional = false)
    private Store storeLogref;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "terminalId")
    private List<Zout> zoutList;

    public Terminal() {
    }

    public Terminal(Integer terminalLogref) {
        this.terminalLogref = terminalLogref;
    }

    public Terminal(Integer terminalLogref, String terminalCode, String terminalName) {
        this.terminalLogref = terminalLogref;
        this.terminalCode = terminalCode;
        this.terminalName = terminalName;
    }

    public Integer getTerminalLogref() {
        return terminalLogref;
    }

    public void setTerminalLogref(Integer terminalLogref) {
        this.terminalLogref = terminalLogref;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Store getStoreLogref() {
        return storeLogref;
    }

    public void setStoreLogref(Store storeLogref) {
        this.storeLogref = storeLogref;
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
        hash += (terminalLogref != null ? terminalLogref.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Terminal)) {
            return false;
        }
        Terminal other = (Terminal) object;
        if ((this.terminalLogref == null && other.terminalLogref != null) || (this.terminalLogref != null && !this.terminalLogref.equals(other.terminalLogref))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Terminal[ terminalLogref=" + terminalLogref + " ]";
    }
    
}
