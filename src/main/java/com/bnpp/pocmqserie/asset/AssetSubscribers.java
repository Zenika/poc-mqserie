package com.bnpp.pocmqserie.asset;

import com.bnpp.pocmqserie.deal.DealJurisdictionUpdated;
import com.bnpp.pocmqserie.deal.DealUpdated;
import com.bnpp.pocmqserie.facility.FacilityJurisdictionUpdated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class AssetSubscribers {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "deal/updated/", subscription = "E2ECC.ASSET.DEAL.UPDATED", containerFactory = "jmsListenerContainerFactory")
    public void onDealUpdated(DealUpdated dealUpdated) {
        System.out.println("AssetSubscribers.onDealUpdated");
        System.out.println("dealUpdated = " + dealUpdated);
    }

    @JmsListener(destination = "facility/jurisdiction/updated/", subscription = "E2ECC.ASSET.FACILITY.JURISDICTION.UPDATED", containerFactory = "jmsListenerContainerFactory3")
    public void onFacilityJurisdictionUpdated(FacilityJurisdictionUpdated facilityJurisdictionUpdated) {
        System.out.println("AssetSubscribers.onFacilityJurisdictionUpdated");
        System.out.println("facilityJurisdictionUpdated = " + facilityJurisdictionUpdated);
    }
}
