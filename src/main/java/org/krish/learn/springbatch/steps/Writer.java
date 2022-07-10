package org.krish.learn.springbatch.steps;

import java.util.List;

import org.krish.learn.kafka.avro.AvroStudent;
import org.krish.learn.springbatch.service.KafkaMessageSender;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<AvroStudent> {

	private String topic;

	private KafkaMessageSender kafkaMessageSender;

	public Writer(KafkaMessageSender kafkaMessageSender, String topic) {
		this.kafkaMessageSender = kafkaMessageSender;
		this.topic = topic;
	}

	@Override
	public void write(List<? extends AvroStudent> items) throws Exception {
		for (AvroStudent item : items) {
			System.out.println("Writing the data " + item);
			kafkaMessageSender.sendMessage(item, Long.toString(item.getId()), topic);
		}

	}

}
