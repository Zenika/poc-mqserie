package com.bnpp.pocmqserie.facility;

import com.bnpp.pocmqserie.EventPublisher;
import com.bnpp.pocmqserie.deal.DealJurisdictionUpdated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FacilityService {

    @Autowired
    private EventPublisher eventPublisher;

    public void updateFacilityJurisdiction(DealJurisdictionUpdated dealJurisdictionUpdated) {
        FacilityJurisdictionUpdated facilityJurisdictionUpdated = new FacilityJurisdictionUpdated(UUID.randomUUID().toString(), dealJurisdictionUpdated.getCorrelationId(), false, "FAC-123", dealJurisdictionUpdated.getImplSites());
        eventPublisher.publish(facilityJurisdictionUpdated);
    }
}
