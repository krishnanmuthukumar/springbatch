package org.krish.learn.springbatch.config;

import javax.sql.DataSource;

import org.krish.learn.kafka.avro.AvroStudent;
import org.krish.learn.springbatch.listener.BatchJobExecutionListener;
import org.krish.learn.springbatch.model.Student;
import org.krish.learn.springbatch.service.KafkaMessageSender;
import org.krish.learn.springbatch.service.StudentService;
import org.krish.learn.springbatch.steps.Processor;
import org.krish.learn.springbatch.steps.StudentPagedItemReader;
import org.krish.learn.springbatch.steps.Writer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfig extends DefaultBatchConfigurer {

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(null);
	}

	@Bean(name = "asyncJobLauncher")
	public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	StudentService studentService;

	@Autowired
	KafkaMessageSender kafkaMessageSender;

	@Autowired
	private StudentPagedItemReader studentPagedItemReader;

	@Value(value = "${spring.kafka.properties.topic}")
	private String topic;

	private int chunksize = 5;

	@Bean
	public Job processJob(@Autowired @Qualifier("studentReaderStep") Step studentStep) {
		System.out.println("processJob is invoked");
		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(listener())
				.flow(studentStep).end().build();
	}

	@Bean("studentReaderStep")
	public Step studentStep(@Autowired @Qualifier("studentDataReader") JdbcPagingItemReader<Student> reader) {
		return stepBuilderFactory.get("step1").<Student, AvroStudent>chunk(chunksize)
				.reader(reader).processor(new Processor()).writer(new Writer(kafkaMessageSender, topic, chunksize))
				.build();
	}

	@Bean("studentDataReader")
	public JdbcPagingItemReader<Student> studentItemReader() {
		return studentPagedItemReader.getPaginationReader();
	}

	public JobExecutionListener listener() {
		return new BatchJobExecutionListener(studentService);
	}

	

}
