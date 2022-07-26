package com.bnpp.pocmqserie;

import com.bnpp.pocmqserie.activity.Activity;
import com.bnpp.pocmqserie.activity.ActivityDirection;
import com.bnpp.pocmqserie.activity.ActivityRepository;
import com.bnpp.pocmqserie.activity.ActivityStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionalEventBus implements EventPublisher {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void publish(DomainEvent domainEvent) {
        try {
            Activity activity = new Activity(UUID.randomUUID().toString(), LocalDateTime.now(), domainEvent.getId(), ActivityDirection.EGRESS, objectMapper.writeValueAsString(domainEvent), domainEvent.getClass().getSimpleName());
            activityRepository.save(activity);
            System.out.println("Activity saved : " + activity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Activity> getPendingEgressActivities() {
        return activityRepository.findAllByStatusAndDirection(ActivityStatus.PENDING, ActivityDirection.EGRESS);
    }

    public void markAsHandled(Activity activity) {
        activity.markAsHandled();
    }
}
