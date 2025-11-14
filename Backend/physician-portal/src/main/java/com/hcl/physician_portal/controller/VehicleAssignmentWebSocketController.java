package com.hcl.physician_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class VehicleAssignmentWebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendAssignmentNotification(Object assignmentPayload) {
        messagingTemplate.convertAndSend("/topic/vehicle-assigned", assignmentPayload);
    }
}
