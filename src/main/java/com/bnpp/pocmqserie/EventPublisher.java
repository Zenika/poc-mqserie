package com.bnpp.pocmqserie;

public interface EventPublisher {

    void publish(DomainEvent domainEvent);
}
