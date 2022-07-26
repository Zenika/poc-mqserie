package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class DealUpdated extends DomainEvent {
    private String dealId;
    private String otherData;

    public DealUpdated(String id, String correlationId, boolean ackRequired, String dealId, String otherData) {
        super(id, correlationId, ackRequired);
        this.dealId = dealId;
        this.otherData = otherData;
    }
}
