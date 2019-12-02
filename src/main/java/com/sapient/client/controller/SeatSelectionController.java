package com.sapient.client.controller;

import java.util.concurrent.ExecutionException;

import com.sapient.model.SeatRequest;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SeatSelectionController {
	
	@Autowired
	private ReplyingKafkaTemplate<String, SeatRequest, String> kafkaTemplate;
	
	//private KafkaTemplate<String, Object> temp;

	@PostMapping(value = "/seat")
	public String sendMessageToKafkaTopic(@RequestBody SeatRequest seatRequest) throws InterruptedException, ExecutionException {
		
		// create producer record
		ProducerRecord<String, SeatRequest> record = new ProducerRecord<String, SeatRequest>("select-seat-request", seatRequest);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "select-seat-response".getBytes()));
		// post in kafka topic
		RequestReplyFuture<String, SeatRequest, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);
		return sendAndReceive.get().value();
		 
		//temp.send("samtest", message);
		
		//return "";
		
	}
}