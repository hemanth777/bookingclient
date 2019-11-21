package com.sapient.client.controller;

import java.util.concurrent.ExecutionException;

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
@RequestMapping(value = "/kafka")
public class SeatSelectionController {
	
	@Autowired
	private ReplyingKafkaTemplate<String, String,String> kafkaTemplate;
	
	//private KafkaTemplate<String, Object> temp;

	@PostMapping(value = "/publish")
	public String sendMessageToKafkaTopic(@RequestParam("message") String message) throws InterruptedException, ExecutionException {
		
		// create producer record
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("samtest", message);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "repsamtest".getBytes()));
		// post in kafka topic
		RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);
		return sendAndReceive.get().value();
		 
		//temp.send("samtest", message);
		
		//return "";
		
	}
}