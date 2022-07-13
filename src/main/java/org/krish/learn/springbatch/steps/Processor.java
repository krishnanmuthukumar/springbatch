package org.krish.learn.springbatch.steps;

import org.krish.learn.kafka.avro.AvroStudent;
import org.krish.learn.springbatch.model.Student;
import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<Student, AvroStudent> {

	@Override
	public AvroStudent process(Student item) throws Exception {
		AvroStudent record = AvroStudent.newBuilder().setId(item.getId()).setName(item.getName())
				.setEmail(item.getEmail()).build();
		System.out.println("processor called");
		return record;
	}

}
