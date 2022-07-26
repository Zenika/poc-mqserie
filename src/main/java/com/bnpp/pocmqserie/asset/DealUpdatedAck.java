package com.bnpp.pocmqserie.asset;

import com.bnpp.pocmqserie.DomainEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class DealUpdatedAck extends DomainEvent {

    public DealUpdatedAck(String id, String correlationId, boolean ackRequired) {
        super(id, correlationId, ackRequired);
    }
}
