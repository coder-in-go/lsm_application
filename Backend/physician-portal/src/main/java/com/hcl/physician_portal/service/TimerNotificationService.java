package com.hcl.physician_portal.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TimerNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void startTimer(String vehicleId, Duration duration) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long totalSeconds = duration.getSeconds();

        scheduler.schedule(() -> sendNotification(vehicleId, "5 minutes remaining"), totalSeconds - 300,
                TimeUnit.SECONDS);
        scheduler.schedule(() -> sendNotification(vehicleId, "Time is up"), totalSeconds, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() -> {
            sendNotification(vehicleId, "Time exceeded...");
        }, totalSeconds + 60, 60, TimeUnit.SECONDS);
    }

    private void sendNotification(String vehicleId, String message) {
        messagingTemplate.convertAndSend("/topic/vehicle/" + vehicleId, message);
    }

}