package com.bnpp.pocmqserie.asset;

import com.bnpp.pocmqserie.EventPublisher;
import com.bnpp.pocmqserie.deal.DealUpdated;
import com.bnpp.pocmqserie.facility.FacilityJurisdictionUpdated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AssetSubscribers {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private EventPublisher eventPublisher;

    @JmsListener(destination = "deal/updated/", subscription = "E2ECC.ASSET.DEAL.UPDATED")
    public void onDealUpdated(DealUpdated dealUpdated) {
        System.out.println("AssetSubscribers.onDealUpdated");
        System.out.println("dealUpdated = " + dealUpdated);

        // Do something

        // ACK
        if (dealUpdated.isAckRequired()) {
            eventPublisher.publish(new DealUpdatedAck(UUID.randomUUID().toString(), dealUpdated.getCorrelationId(), false));
        }
    }

    @JmsListener(destination = "facility/jurisdiction/updated/", subscription = "E2ECC.ASSET.FACILITY.JURISDICTION.UPDATED")
    public void onFacilityJurisdictionUpdated(FacilityJurisdictionUpdated facilityJurisdictionUpdated) {
        System.out.println("AssetSubscribers.onFacilityJurisdictionUpdated");
        System.out.println("facilityJurisdictionUpdated = " + facilityJurisdictionUpdated);
    }
}
