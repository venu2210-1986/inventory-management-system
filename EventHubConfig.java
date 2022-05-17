package com.venu.eventhub.demo.configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.core.amqp.AmqpTransportType;
import com.azure.core.amqp.ProxyAuthenticationType;
import com.azure.core.amqp.ProxyOptions;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;
import com.azure.messaging.eventhubs.EventHubProducerClient;

@Configuration
public class EventHubConfig {

	@Value("${eventHub.connectionString}")
	private String connectionString;

	@Value("${eventHub.name}")
	private String eventHubName;
/*
	@Bean
	public EventHubClient setupEventHubConnection() throws IOException, EventHubException {
		return EventHubClient.createFromConnectionStringSync(connectionString, Executors.newScheduledThreadPool(5));
	}*/

	@Value("${eventHub.storage.consumerGroupName}")
	private String consumerGroupName;

	@Value("${eventHub.storage.hostNamePrefix}")
	private String hostNamePrefix;

	@Value("${eventHub.storage.storageConnectionString}")
	private String storageConnectionString;

	@Value("${eventHub.storage.storageContainerName}")
	private String storageContainerName;

	/*
	 * @Bean public EventProcessorHost createEventHubProcessorHost() { return
	 * EventProcessorHost.EventProcessorHostBuilder
	 * .newBuilder(EventProcessorHost.createHostName(hostNamePrefix),
	 * consumerGroupName)
	 * .useAzureStorageCheckpointLeaseManager(storageConnectionString,
	 * storageContainerName,null) .useEventHubConnectionString(connectionString,
	 * eventHubName) .build(); }
	 */

	@Bean
	public EventHubProducerAsyncClient createEventHubProcessorHost() {
		
		/*ProxyOptions proxyOptions = new ProxyOptions(ProxyAuthenticationType.DIGEST,
	            new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("20.42.74.35", 80)),
	            "digest-user", "digest-user-password");*/

	        // Instantiate a client that will be used to call the service.
	        EventHubProducerAsyncClient producer = new EventHubClientBuilder()
	            .transportType(AmqpTransportType.AMQP_WEB_SOCKETS)
	            //.proxyOptions(proxyOptions)
	            .connectionString(connectionString)
	            .buildAsyncProducerClient();

		return producer;
	}

}
