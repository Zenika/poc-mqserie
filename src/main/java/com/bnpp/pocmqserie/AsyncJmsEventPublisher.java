package com.bnpp.pocmqserie;

import com.bnpp.pocmqserie.asset.DealUpdatedAck;
import com.bnpp.pocmqserie.deal.DealJurisdictionUpdated;
import com.bnpp.pocmqserie.deal.DealUpdated;
import com.bnpp.pocmqserie.facility.FacilityJurisdictionUpdated;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncJmsEventPublisher {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionalEventBus transactionalEventBus;

    @Async
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void send() {
        transactionalEventBus.getPendingEgressActivities().forEach(activity -> {
            emitMessage(activity.getType(), activity.getContent());
            transactionalEventBus.markAsHandled(activity);
        });
    }

    private void emitMessage(String type, String content) {
        System.out.println("AsyncJmsEventPublisher.emitMessage");
        System.out.println("type = " + type + ", content = " + content);
        try {
            if (type.equalsIgnoreCase("facilityjurisdictionupdated")) {
                jmsTemplate.convertAndSend("facility/jurisdiction/updated/", objectMapper.readValue(content, FacilityJurisdictionUpdated.class));
            } else if (type.equalsIgnoreCase("dealupdatedack")) {
                jmsTemplate.convertAndSend("deal/updated/ack/", objectMapper.readValue(content, DealUpdatedAck.class));
            } else if (type.equalsIgnoreCase("dealupdated")) {
                jmsTemplate.convertAndSend("deal/updated/", objectMapper.readValue(content, DealUpdated.class));
            } else if (type.equalsIgnoreCase("dealjurisdictionupdated")) {
                jmsTemplate.convertAndSend("deal/jurisdiction/updated/", objectMapper.readValue(content, DealJurisdictionUpdated.class));
            } else {
                System.out.println("Does not know where to send event " + type);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("unable to send the message", e);
        }
    }
}
