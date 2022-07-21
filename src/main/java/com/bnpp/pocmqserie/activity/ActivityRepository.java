package com.bnpp.pocmqserie.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {
    List<Activity> findAllByStatusAndDirection(ActivityStatus status, ActivityDirection direction);

    List<Activity> findByOrderByOccuredOnDesc();
}
