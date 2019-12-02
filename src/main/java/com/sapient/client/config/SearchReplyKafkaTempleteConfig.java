package com.sapient.client.config;

import com.sapient.response.SearchResponse;
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
public class SearchReplyKafkaTempleteConfig {

	@Bean
	public ReplyingKafkaTemplate<String, String, SearchResponse> replyKafkaTemplate(ProducerFactory<String, String> pf,
			KafkaMessageListenerContainer<String, SearchResponse> container) {
		return new ReplyingKafkaTemplate<>(pf, container);

	}

	@Bean
	public KafkaMessageListenerContainer<String, SearchResponse> replyContainer(ConsumerFactory<String, SearchResponse> cf) {
		ContainerProperties containerProperties = new ContainerProperties("search-movie-response");
		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}
	
	 @Bean
	  public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf) {
	    return new KafkaTemplate<>(pf);
	  }
	 
	 @Bean
	  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SearchResponse>> kafkaListenerContainerFactory(
			  ConsumerFactory<String, SearchResponse> cf,ProducerFactory<String, String> pf) {
	    ConcurrentKafkaListenerContainerFactory<String, SearchResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(cf);
	    factory.setReplyTemplate(kafkaTemplate(pf));
	    return factory;
	  }

}
