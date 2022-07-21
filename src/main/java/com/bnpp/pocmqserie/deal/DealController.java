package com.bnpp.pocmqserie.deal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class DealController {

    @Autowired
    private DealService dealService;
    @Autowired
    private DealRepository dealRepository;

    @GetMapping("/deals")
    public List<Deal> fetchDeals() {
        try {
            return dealRepository.findAll();
        } catch (JmsException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PostMapping("/deals")
    public String createNewDeal(@RequestBody String dealName) {
        try {
            return dealService.createDeal(dealName);
        } catch (JmsException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping("/deals/{dealId}")
    public void updateDeal(@PathVariable String dealId, @RequestBody String dealName) {
        try {
            dealService.updateDeal(dealId, dealName);
        } catch (JmsException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping("/deals/{dealId}/jurisdiction")
    public void updateDealJurisdiction(@PathVariable String dealId, @RequestBody List<String> implSites) {
        try {
            dealService.updateJurisdiction(dealId, implSites);
        } catch (JmsException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    // ISSUE HERE : With a local Transaction manager, the database is rolledback but the jms message is sent.
    @PutMapping("/deals/{dealId}/jurisdiction-with-failure-after-message-sent")
    public void updateJurisdictionWithFailureAfterMessageSent(@PathVariable String dealId, @RequestBody List<String> implSites) {
        try {
            dealService.updateJurisdictionWithFailureAfterMessageSent(dealId, implSites);
        } catch (JmsException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
