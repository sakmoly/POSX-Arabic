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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jomit
 */
@Entity
@Table(name = "pos_application")
@NamedQueries({
    @NamedQuery(name = "Application.findAll", query = "SELECT a FROM Application a")})
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "app_id")
    private Integer appId;
    @Basic(optional = false)
    @Column(name = "app_name")
    private String appName;
    @Column(name = "app_desc")
    private String appDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appId")
    private List<ApplicationPreferenceValue> applicationPreferenceValueList;

    public Application() {
    }

    public Application(Integer appId) {
        this.appId = appId;
    }

    public Application(Integer appId, String appName) {
        this.appId = appId;
        this.appName = appName;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public List<ApplicationPreferenceValue> getApplicationPreferenceValueList() {
        return applicationPreferenceValueList;
    }

    public void setApplicationPreferenceValueList(List<ApplicationPreferenceValue> applicationPreferenceValueList) {
        this.applicationPreferenceValueList = applicationPreferenceValueList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appId != null ? appId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
        if ((this.appId == null && other.appId != null) || (this.appId != null && !this.appId.equals(other.appId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnooc.desktop.app.posx.model.Application[ appId=" + appId + " ]";
    }
    
}
