package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.DomainEvent;

public class DealUpdated implements DomainEvent {
    private String id;
    private String dealId;
    private String otherData;
    private String correlationId;

    public DealUpdated(String id, String dealId, String otherData, String correlationId) {
        this.id = id;
        this.dealId = dealId;
        this.otherData = otherData;
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public DealUpdated() {
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    @Override
    public String toString() {
        return "DealUpdated{" +
                "dealId='" + dealId + '\'' +
                ", otherData='" + otherData + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }
}
