/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
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
@Table(name = "pos_doc_sequence")
public class DocSequence implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "doc_seq_logref")
    private Integer docSeqLogref;
    @Basic(optional = false)
    @Column(name = "terminal_code")
    private String terminalCode;
    @Basic(optional = false)
    @Column(name = "doc_seq_next_value")
    private int docSeqNextValue;
    @JoinColumn(name = "store_logref", referencedColumnName = "store_logref")
    @ManyToOne(optional = false)
    private Store storeLogref;

    public DocSequence() {
    }

    public DocSequence(Integer docSeqLogref) {
        this.docSeqLogref = docSeqLogref;
    }

    public DocSequence(Integer docSeqLogref, String terminalCode, int docSeqNextValue) {
        this.docSeqLogref = docSeqLogref;
        this.terminalCode = terminalCode;
        this.docSeqNextValue = docSeqNextValue;
    }

    public Integer getDocSeqLogref() {
        return docSeqLogref;
    }

    public void setDocSeqLogref(Integer docSeqLogref) {
        this.docSeqLogref = docSeqLogref;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public int getDocSeqNextValue() {
        return docSeqNextValue;
    }

    public void setDocSeqNextValue(int docSeqNextValue) {
        this.docSeqNextValue = docSeqNextValue;
    }

    public Store getStoreLogref() {
        return storeLogref;
    }

    public void setStoreLogref(Store storeLogref) {
        this.storeLogref = storeLogref;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docSeqLogref != null ? docSeqLogref.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocSequence)) {
            return false;
        }
        DocSequence other = (DocSequence) object;
        if ((this.docSeqLogref == null && other.docSeqLogref != null) || (this.docSeqLogref != null && !this.docSeqLogref.equals(other.docSeqLogref))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.DocSequence[ docSeqLogref=" + docSeqLogref + " ]";
    }
    
}
