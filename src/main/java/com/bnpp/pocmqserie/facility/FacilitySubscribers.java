package com.bnpp.pocmqserie.facility;

import com.bnpp.pocmqserie.deal.DealJurisdictionUpdated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class FacilitySubscribers {

    @Autowired
    private FacilityService facilityService;

    @JmsListener(destination = "deal/jurisdiction/updated/", subscription = "E2ECC.FACILITY.DEAL.JURISDICTION.UPDATED")
    public void onDealJurisdictionUpdated(DealJurisdictionUpdated dealJurisdictionUpdated) {
        System.out.println("FacilitySubscribers.onDealJurisdictionUpdated");
        System.out.println("dealJurisdictionUpdated = " + dealJurisdictionUpdated);

        facilityService.updateFacilityJurisdiction(dealJurisdictionUpdated);
    }
}
