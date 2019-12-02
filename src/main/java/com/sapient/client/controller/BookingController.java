package com.sapient.client.controller;

import java.util.concurrent.ExecutionException;

import com.sapient.model.BookingRequest;
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
public class BookingController {
	
	@Autowired
	private ReplyingKafkaTemplate<String, BookingRequest,String> kafkaTemplate;
	
	//private KafkaTemplate<String, Object> temp;

	/*@PostMapping(value = "/publish")
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
		
	}*/

	@PostMapping(value = "bookmovie")
	public String newBooking(@RequestBody BookingRequest bookingRequest) throws ExecutionException, InterruptedException {
		// create producer record
		ProducerRecord<String, BookingRequest> record = new ProducerRecord<String, BookingRequest>("new-booking-request", bookingRequest);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "new-booking-response".getBytes()));
		// post in kafka topic
		RequestReplyFuture<String, BookingRequest, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);
		return sendAndReceive.get().value();
	}
}