package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DealService {

    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public String createDeal(String dealName) {
        String dealId = UUID.randomUUID().toString();
        Deal deal = new Deal(dealId, dealName);
        dealRepository.save(deal);
        eventPublisher.publish(new DealCreated(UUID.randomUUID().toString(), deal));
        return dealId;
    }

    @Transactional
    public void updateDeal(String dealId, String dealName) {
        Deal deal = dealRepository.findById(dealId).orElseThrow();
        deal.update(dealName);
        dealRepository.save(deal);
        String eventId = UUID.randomUUID().toString();
        eventPublisher.publish(new DealUpdated(eventId, dealId, dealName, eventId));
    }

    @Transactional
    public void updateJurisdiction(String dealId, List<String> implSites) {
        Deal deal = dealRepository.findById(dealId).orElseThrow();
        deal.updateJurisdiction(implSites);
        dealRepository.save(deal);
        String eventId = UUID.randomUUID().toString();
        eventPublisher.publish(new DealJurisdictionUpdated(eventId, dealId, implSites, eventId));
    }

    @Transactional
    public void updateJurisdictionWithFailureAfterMessageSent(String dealId, List<String> implSites) {
        updateJurisdiction(dealId, implSites);
        throw new RuntimeException("onpurpose fail");
    }
}
