package com.venu.eventhub.demo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.venu.eventhub.demo.model.EventPayload;

import reactor.core.publisher.Mono;

@Service
public class EventHubService {

	private final EventHubProducerAsyncClient eventHubClient;

	@Autowired
	public EventHubService(EventHubProducerAsyncClient eventHubClient) {
		this.eventHubClient = eventHubClient;
	}

	public void sendEvent(EventPayload test) {

        byte[] bytes = SerializationUtils.serialize(test);
        System.out.println("Success:::");
        
        // sample events in an array
        // Create a batch and add a sample log to it
        eventHubClient.createBatch().flatMap(batch -> {
            batch.tryAdd(new EventData("test-event-hub-producer"));
            batch.tryAdd(new EventData("test-event-hub-producer-02"));

            return eventHubClient.send(batch);
        }).subscribe(unused -> {}, 
         error -> System.err.println("Couldn't send logs, there's an error: " + error.getStackTrace()), 
         () -> {
            System.out.println("Send complete!");
            // Close the connection
            eventHubClient.close();
           // Count down the latch to complete the process
        });
       // log.info("Sending message to the event hub {}", eventHubClient.getEventHubName());
       // eventHubClient.send(EventData.create(Objects.requireNonNull(bytes)), test.toString());
    }

}
