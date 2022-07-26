package com.bnpp.pocmqserie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class DomainEvent {
    private String id;
    private String correlationId;
    private boolean ackRequired;

}
