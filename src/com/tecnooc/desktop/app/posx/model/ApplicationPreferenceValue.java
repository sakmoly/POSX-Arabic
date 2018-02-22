/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_application_preference_value")
@NamedQueries({
    @NamedQuery(name = "ApplicationPreferenceValue.findAll", query = "SELECT a FROM ApplicationPreferenceValue a")})
public class ApplicationPreferenceValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "app_pref_id")
    private Integer appPrefId;
    @Basic(optional = false)
    @Column(name = "app_pref_value")
    private String appPrefValue;
    @JoinColumn(name = "app_id", referencedColumnName = "app_id")
    @ManyToOne(optional = false)
    private Application appId;

    public ApplicationPreferenceValue() {
    }

    public ApplicationPreferenceValue(Integer appPrefId) {
        this.appPrefId = appPrefId;
    }

    public ApplicationPreferenceValue(Integer appPrefId, String appPrefValue) {
        this.appPrefId = appPrefId;
        this.appPrefValue = appPrefValue;
    }

    public Integer getAppPrefId() {
        return appPrefId;
    }

    public void setAppPrefId(Integer appPrefId) {
        this.appPrefId = appPrefId;
    }

    public String getAppPrefValue() {
        return appPrefValue;
    }

    public void setAppPrefValue(String appPrefValue) {
        this.appPrefValue = appPrefValue;
    }

    public Application getAppId() {
        return appId;
    }

    public void setAppId(Application appId) {
        this.appId = appId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appPrefId != null ? appPrefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationPreferenceValue)) {
            return false;
        }
        ApplicationPreferenceValue other = (ApplicationPreferenceValue) object;
        if ((this.appPrefId == null && other.appPrefId != null) || (this.appPrefId != null && !this.appPrefId.equals(other.appPrefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.ApplicationPreferenceValue[ appPrefId=" + appPrefId + " ]";
    }
    
}
