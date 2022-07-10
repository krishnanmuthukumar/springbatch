package org.krish.learn.springbatch.service;

import org.krish.learn.kafka.avro.AvroStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaMessageSender {

	@Autowired
	KafkaTemplate<String, AvroStudent> kafkaTemplate;

	public void sendMessage(AvroStudent message, String key, String topicName) {
		
		System.out.println(kafkaTemplate.getProducerFactory().getConfigurationProperties());
		ListenableFuture<SendResult<String, AvroStudent>> future = kafkaTemplate.send(topicName,key, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, AvroStudent>>() {
			@Override
			public void onSuccess(SendResult<String, AvroStudent> result) {
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
