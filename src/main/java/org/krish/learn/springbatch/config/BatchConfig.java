package org.krish.learn.springbatch.config;

import org.krish.learn.kafka.avro.AvroStudent;
import org.krish.learn.springbatch.listener.BatchJobExecutionListener;
import org.krish.learn.springbatch.model.Student;
import org.krish.learn.springbatch.service.KafkaMessageSender;
import org.krish.learn.springbatch.service.StudentService;
import org.krish.learn.springbatch.steps.Processor;
import org.krish.learn.springbatch.steps.Reader;
import org.krish.learn.springbatch.steps.Writer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	KafkaMessageSender kafkaMessageSender;
	
	@Value(value = "${spring.kafka.properties.topic}")
	private String topic;
	
	private int chunksize = 2;

	@Bean
	public Job processJob() {
		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(listener()).flow(step1())
				.end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Student, AvroStudent>chunk(chunksize).reader(new Reader(studentService)).processor(new Processor())
				.writer(new Writer(kafkaMessageSender, topic, chunksize)).build();
	}

	public JobExecutionListener listener() {
		return new BatchJobExecutionListener(studentService);
	}

}
