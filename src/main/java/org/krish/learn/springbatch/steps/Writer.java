package org.krish.learn.springbatch.steps;

import java.util.List;

import org.krish.learn.springbatch.service.KafkaMessageSender;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<String> {

	private String topic;

	private KafkaMessageSender kafkaMessageSender;

	public Writer(KafkaMessageSender kafkaMessageSender, String topic) {
		this.kafkaMessageSender = kafkaMessageSender;
		this.topic = topic;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {
		for (String item : items) {
			System.out.println("Writing the data " + item);
			kafkaMessageSender.sendMessage(item);
		}

	}

}
