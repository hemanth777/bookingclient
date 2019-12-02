package com.sapient.client.config;

import com.sapient.criteria.SelectionCriteria;
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
public class SeatSelectionReplyKafkaTempleteConfig {

	@Bean
	public ReplyingKafkaTemplate<String, SelectionCriteria, MovieDTO> replyKafkaTemplate(ProducerFactory<String, SelectionCriteria> pf,
			KafkaMessageListenerContainer<String, MovieDTO> container) {
		return new ReplyingKafkaTemplate<>(pf, container);

	}

	@Bean
	public KafkaMessageListenerContainer<String, MovieDTO> replyContainer(ConsumerFactory<String, MovieDTO> cf) {
		ContainerProperties containerProperties = new ContainerProperties("select-seat-response");
		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}
	
	 @Bean
	  public KafkaTemplate<String, SelectionCriteria> kafkaTemplate(ProducerFactory<String, SelectionCriteria> pf) {
	    return new KafkaTemplate<>(pf);
	  }
	 
	 @Bean
	  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MovieDTO>> kafkaListenerContainerFactory(
			  ConsumerFactory<String, MovieDTO> cf,ProducerFactory<String, SelectionCriteria> pf) {
	    ConcurrentKafkaListenerContainerFactory<String, MovieDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(cf);
	    factory.setReplyTemplate(kafkaTemplate(pf));
	    return factory;
	  }

}
