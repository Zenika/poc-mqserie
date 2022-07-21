package com.bnpp.pocmqserie;

import com.bnpp.pocmqserie.activity.Activity;
import com.bnpp.pocmqserie.activity.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventBusController {

    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping("/activities")
    public List<Activity> fetchActivities() {
        return activityRepository.findByOrderByOccuredOnDesc();
    }
}
