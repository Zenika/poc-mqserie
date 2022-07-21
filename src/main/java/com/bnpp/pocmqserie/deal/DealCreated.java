package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.DomainEvent;

public class DealCreated implements DomainEvent {
    private String id;
    private Deal deal;

    public DealCreated(String id, Deal deal) {
        this.id = id;
        this.deal = deal;
    }

    public DealCreated() {
    }


    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    @Override
    public String toString() {
        return "DealCreated{" +
                "deal=" + deal +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }
}
