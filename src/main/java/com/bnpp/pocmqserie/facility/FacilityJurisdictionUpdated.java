package com.bnpp.pocmqserie.facility;

import com.bnpp.pocmqserie.DomainEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class FacilityJurisdictionUpdated extends DomainEvent {
    private String facilityId;
    private List<String> implSites;

    public FacilityJurisdictionUpdated(String id, String correlationId, boolean ackRequired, String facilityId, List<String> implSites) {
        super(id, correlationId, ackRequired);
        this.facilityId = facilityId;
        this.implSites = implSites;
    }
}
