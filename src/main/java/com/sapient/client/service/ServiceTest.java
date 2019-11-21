package com.sapient.client.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class ServiceTest {
	
	@KafkaListener(topics = "samtest",groupId = "test")
	@SendTo()
	public String list(String message) {
		
		System.out.println(message);
		return "Sucess";
	}

}
