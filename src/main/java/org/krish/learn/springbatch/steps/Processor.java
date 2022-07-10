package org.krish.learn.springbatch.steps;

import org.krish.learn.springbatch.model.Student;
import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<Student, String> {

	@Override
	public String process(Student item) throws Exception {
		// TODO Auto-generated method stub
		return item.toString().toUpperCase();
	}

}
