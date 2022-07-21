package com.bnpp.pocmqserie.deal;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deal {
    @Id
    private String dealId;
    private String name;
    @ElementCollection
    private List<String> implSites = new ArrayList<>();

    public Deal(String dealId, String name) {
        this.dealId = dealId;
        this.name = name;
    }

    public Deal() {

    }

    public void update(String dealName) {
        this.name = dealName;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateJurisdiction(List<String> implSites) {
        this.implSites = implSites;
    }

    public List<String> getImplSites() {
        return implSites;
    }

    public void setImplSites(List<String> implSites) {
        this.implSites = implSites;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "dealId='" + dealId + '\'' +
                ", name='" + name + '\'' +
                ", implSites=" + implSites +
                '}';
    }
}
