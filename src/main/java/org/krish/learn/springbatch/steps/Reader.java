package org.krish.learn.springbatch.steps;

import java.util.ArrayList;
import java.util.List;

import org.krish.learn.springbatch.model.Student;
import org.krish.learn.springbatch.service.StudentService;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<Student> {

	private int count = 0;

	private List<Student> listStudents = new ArrayList<Student>();

	private StudentService studentService;

	public Reader(StudentService studentService) {
		this.studentService = studentService;
	}

	@Override
	public Student read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		listStudents = studentService.getAllStudents();
		if (count < listStudents.size()) {
			System.out.println("count::" + count);
			return listStudents.get(count++);
		} else {
			count = 0;
		}
		return null;
	}

}
