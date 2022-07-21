package com.bnpp.pocmqserie.facility;

import com.bnpp.pocmqserie.DomainEvent;

import java.util.List;

public class FacilityJurisdictionUpdated implements DomainEvent {
    private String id;
    private String facilityId;
    private List<String> implSites;
    private String correlationId;

    public FacilityJurisdictionUpdated(String id, String facilityId, List<String> implSites, String correlationId) {
        this.id = id;
        this.facilityId = facilityId;
        this.implSites = implSites;
        this.correlationId = correlationId;
    }

    public FacilityJurisdictionUpdated() {
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public List<String> getImplSites() {
        return implSites;
    }

    public void setImplSites(List<String> implSites) {
        this.implSites = implSites;
    }

    @Override
    public String toString() {
        return "FacilityJurisdictionUpdated{" +
                "facilityId='" + facilityId + '\'' +
                ", implSites=" + implSites +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }
}
