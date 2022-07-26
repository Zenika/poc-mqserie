package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.DomainEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class DealJurisdictionUpdated extends DomainEvent {
    private String dealId;
    private List<String> implSites;

    public DealJurisdictionUpdated(String id, String correlationId, boolean ackRequired, String dealId, List<String> implSites) {
        super(id, correlationId, ackRequired);
        this.dealId = dealId;
        this.implSites = implSites;
    }
}
