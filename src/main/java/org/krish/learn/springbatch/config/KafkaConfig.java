package org.krish.learn.springbatch.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.krish.learn.kafka.avro.AvroStudent;
import org.krish.learn.kafka.avro.AvroStudentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

@Configuration
public class KafkaConfig {

	@Autowired
	private KafkaProperties kafkaProperties;

	@Bean
	public ProducerFactory<String, AvroStudentWrapper> producerFactory() {

		Map<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		return new DefaultKafkaProducerFactory<String, AvroStudentWrapper>(configProps);
	}

	@Bean
	public KafkaTemplate<String, AvroStudentWrapper> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
