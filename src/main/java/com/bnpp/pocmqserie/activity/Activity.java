package com.bnpp.pocmqserie.activity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Activity {
    @Id
    private String id;
    private LocalDateTime occuredOn;
    private String eventId;
    private ActivityDirection direction;
    private String content;
    private String type;
    private ActivityStatus status;
    private LocalDateTime deliveredOn;

    public Activity(String id, LocalDateTime occuredOn, String eventId, ActivityDirection direction, String content, String type) {
        this.id = id;
        this.occuredOn = occuredOn;
        this.eventId = eventId;
        this.direction = direction;
        this.content = content;
        this.type = type;
        this.status = ActivityStatus.PENDING;
    }

    public Activity() {
    }

    public void markAsHandled() {
        this.status = ActivityStatus.HANDLED;
        this.deliveredOn = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getOccuredOn() {
        return occuredOn;
    }

    public void setOccuredOn(LocalDateTime occuredOn) {
        this.occuredOn = occuredOn;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ActivityDirection getDirection() {
        return direction;
    }

    public void setDirection(ActivityDirection direction) {
        this.direction = direction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public LocalDateTime getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(LocalDateTime deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", occuredOn=" + occuredOn +
                ", eventId='" + eventId + '\'' +
                ", direction=" + direction +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", deliveredOn=" + deliveredOn +
                '}';
    }
}
