package org.krish.learn.springbatch.service;

import org.krish.learn.kafka.avro.AvroStudentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaMessageSender {

	@Autowired
	KafkaTemplate<String, AvroStudentWrapper> kafkaTemplate;

	public void sendMessage(AvroStudentWrapper message, String key, String topicName) {
		
		System.out.println(kafkaTemplate.getProducerFactory().getConfigurationProperties());
		ListenableFuture<SendResult<String, AvroStudentWrapper>> future = kafkaTemplate.send(topicName,key, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, AvroStudentWrapper>>() {
			@Override
			public void onSuccess(SendResult<String, AvroStudentWrapper> result) {
				System.out.println("Sent message=[" + message + "] with offset=" + result.getRecordMetadata().offset()
						+ " and partition=" + result.getRecordMetadata().partition());
			}

			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			}
		});
	}

}
