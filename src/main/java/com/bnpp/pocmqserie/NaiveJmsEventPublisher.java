package com.bnpp.pocmqserie;

import com.bnpp.pocmqserie.deal.DealJurisdictionUpdated;
import com.bnpp.pocmqserie.deal.DealUpdated;
import com.bnpp.pocmqserie.facility.FacilityJurisdictionUpdated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

//@Service
public class NaiveJmsEventPublisher implements EventPublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void publish(DomainEvent domainEvent) {
        if (domainEvent instanceof FacilityJurisdictionUpdated) {
            jmsTemplate.convertAndSend("facility/jurisdiction/updated/", domainEvent);
        } else if (domainEvent instanceof DealUpdated) {
            jmsTemplate.convertAndSend("deal/updated/", domainEvent);
        } else if (domainEvent instanceof DealJurisdictionUpdated) {
            jmsTemplate.convertAndSend("deal/jurisdiction/updated/", domainEvent);
        } else {
            System.out.println("Does not know where to send event " + domainEvent);
        }
    }
}
