package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.DomainEvent;

import java.util.List;

public class DealJurisdictionUpdated implements DomainEvent {
    private String id;
    private String dealId;
    private List<String> implSites;
    private String correlationId;

    public DealJurisdictionUpdated(String id, String dealId, List<String> implSites, String correlationId) {
        this.id = id;
        this.dealId = dealId;
        this.implSites = implSites;
        this.correlationId = correlationId;
    }

    public DealJurisdictionUpdated() {
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public List<String> getImplSites() {
        return implSites;
    }

    public void setImplSites(List<String> implSites) {
        this.implSites = implSites;
    }

    @Override
    public String toString() {
        return "DealJurisdictionUpdated{" +
                "dealId='" + dealId + '\'' +
                ", implSites=" + implSites +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }
}
