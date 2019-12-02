package com.sapient.client.config;

import com.sapient.dto.MovieDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class BookingReplyKafkaTempleteConfig {

	@Bean
	public ReplyingKafkaTemplate<String, MovieDTO, String> replyKafkaTemplate(ProducerFactory<String, MovieDTO> pf,
			KafkaMessageListenerContainer<String, String> container) {
		return new ReplyingKafkaTemplate<>(pf, container);

	}

	@Bean
	public KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
		ContainerProperties containerProperties = new ContainerProperties("new-booking-response");
		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}
	
	 @Bean
	  public KafkaTemplate<String, MovieDTO> kafkaTemplate(ProducerFactory<String, MovieDTO> pf) {
	    return new KafkaTemplate<>(pf);
	  }
	 
	 @Bean
	  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
			  ConsumerFactory<String, String> cf,ProducerFactory<String, MovieDTO> pf) {
	    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(cf);
	    factory.setReplyTemplate(kafkaTemplate(pf));
	    return factory;
	  }

}
