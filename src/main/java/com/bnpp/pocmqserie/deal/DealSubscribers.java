package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.asset.DealUpdatedAck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DealSubscribers {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "deal/updated/ack/", subscription = "E2ECC.DEAL.UPDATED.ACK")
    public void onDealUpdatedAck(DealUpdatedAck event) {
        System.out.println("DealSubscribers.onDealUpdatedAck");
        System.out.println("event = " + event);
    }

}
