package com.sapient.client.controller;

import java.util.concurrent.ExecutionException;

import com.sapient.model.SearchRequest;
import com.sapient.model.SearchResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SearchController {
	
	@Autowired
	private ReplyingKafkaTemplate<String, SearchRequest,SearchResponse> kafkaTemplate;
	
	//private KafkaTemplate<String, Object> temp;

	@PostMapping(value = "/search")
	public SearchResponse search(@RequestParam("message") SearchRequest searchRequest) throws InterruptedException, ExecutionException {
		
		// create producer record
		ProducerRecord<String, SearchRequest> record = new ProducerRecord<String, SearchRequest>("search-movie-request", searchRequest);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "search-movie-response".getBytes()));
		// post in kafka topic
		RequestReplyFuture<String, SearchRequest, SearchResponse> sendAndReceive = kafkaTemplate.sendAndReceive(record);
		return sendAndReceive.get().value();
		 
		//temp.send("samtest", message);
		
		//return "";
		
	}
}