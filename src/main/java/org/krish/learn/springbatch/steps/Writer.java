package org.krish.learn.springbatch.steps;

import java.util.ArrayList;
import java.util.List;

import org.krish.learn.kafka.avro.AvroStudent;
import org.krish.learn.kafka.avro.AvroStudentWrapper;
import org.krish.learn.springbatch.service.KafkaMessageSender;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<AvroStudent> {

	private String topic;

	private KafkaMessageSender kafkaMessageSender;

	private int chunkSize;

	private ExecutionContext jobContext;

	public Writer(KafkaMessageSender kafkaMessageSender, String topic, int chunkSize) {
		this.kafkaMessageSender = kafkaMessageSender;
		this.topic = topic;
		this.chunkSize = chunkSize;
		System.out.println("writer called");
	}

	@Override
	public void write(List<? extends AvroStudent> items) throws Exception {

		List<AvroStudent> listAvroStudent = new ArrayList<AvroStudent>();

		for (AvroStudent item : items) {
			listAvroStudent.add(item);
			System.out.println("writer::" + item.toString());
		}

		String strtotalcount = jobContext.get("totalcount").toString();
		System.out.println("writer strtotalcount:" + strtotalcount);

		AvroStudentWrapper record = AvroStudentWrapper.newBuilder().setAvrostudent(listAvroStudent)
				.setRecordscount(Integer.toString(chunkSize)).setTotalrecords(strtotalcount).build();
		kafkaMessageSender.sendMessage(record, "1", topic);
	}

	@BeforeStep
	public void getTotalCount(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		this.jobContext = jobExecution.getExecutionContext();
	}

}
