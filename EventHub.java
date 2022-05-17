package com.venu.eventhub.demo.service;

import java.util.Arrays;
import java.util.List;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;


public class EventHub {
	
	private static final String connectionString = "Endpoint=sb://venu-name.servicebus.windows.net/;SharedAccessKeyName=venu-policy;SharedAccessKey=/H3XhYJL2wuDXpm5qKB+hFY6uVtPnQRHGnRzEFN3KBE=;EntityPath=venu-event";
    private static final String eventHubName = "venu-event";

    public static void main(String[] args) {
        publishEvents();
    }
	/**
     * Code sample for publishing events.
     * @throws IllegalArgumentException if the EventData is bigger than the max batch size.
     */
    public static void publishEvents() {
        // create a producer client
        EventHubProducerClient producer = new EventHubClientBuilder()
            .connectionString(connectionString, eventHubName)
            .buildProducerClient();

        // sample events in an array
        List<EventData> allEvents = Arrays.asList(new EventData("Foo"), new EventData("Bar"));

        // create a batch
        EventDataBatch eventDataBatch = producer.createBatch();

        for (EventData eventData : allEvents) {
            // try to add the event from the array to the batch
            if (!eventDataBatch.tryAdd(eventData)) {
                // if the batch is full, send it and then create a new batch
                producer.send(eventDataBatch);
                eventDataBatch = producer.createBatch();

                // Try to add that event that couldn't fit before.
                if (!eventDataBatch.tryAdd(eventData)) {
                    throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
                        + eventDataBatch.getMaxSizeInBytes());
                }
            }
        }
        // send the last batch of remaining events
        if (eventDataBatch.getCount() > 0) {
            producer.send(eventDataBatch);
        }
        producer.close();
    }
}
