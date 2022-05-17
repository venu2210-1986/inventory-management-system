package com.venu.eventhub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venu.eventhub.demo.model.EventPayload;
import com.venu.eventhub.demo.service.EventHubService;

@RestController
public class Controller {

    private final EventHubService eventHubService;

    @Autowired
    public Controller(EventHubService eventHubService) {
        this.eventHubService = eventHubService;
    }

    @PostMapping(path = "/eventhub/send")
    public ResponseEntity sendEvent(@RequestBody EventPayload payload) {
        try {
          //  log.info("Eventhub send endpoint called, sending {} to event hub..", payload.toString());
            eventHubService.sendEvent(payload);
        } catch (Exception e) {
            //log.error("An error arose sending a message to event hub: " + e);
            return new ResponseEntity<Exception>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
