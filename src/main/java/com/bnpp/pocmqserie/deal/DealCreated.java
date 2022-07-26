package com.bnpp.pocmqserie.deal;

import com.bnpp.pocmqserie.DomainEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class DealCreated extends DomainEvent {
    private Deal deal;

    public DealCreated(String id, String correlationId, boolean ackRequired, Deal deal) {
        super(id, correlationId, ackRequired);
        this.deal = deal;
    }
}
