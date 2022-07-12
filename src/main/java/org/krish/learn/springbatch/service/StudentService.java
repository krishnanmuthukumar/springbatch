package org.krish.learn.springbatch.service;

import java.util.List;
import java.util.Optional;

import org.krish.learn.springbatch.model.Student;
import org.krish.learn.springbatch.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	public List<Student> getAllStudents() {
		System.out.println("findAll() method called!");
		return studentRepository.findAll();
	}

	public Optional<Student> getStudentById(int id) {
		return studentRepository.findById(id);
	}

	public Student addStudent(Student student) {
		return studentRepository.save(student);
	}

	public void deleteStudent(int id) {
		studentRepository.deleteById(id);
	}

	public Student updateStudent(Student student) {

		if (studentRepository.findById(student.getId()).isPresent()) {
			Student objStudent = studentRepository.findById(student.getId()).get();
			objStudent.setName(student.getName());
			objStudent.setEmail(student.getEmail());
			return studentRepository.save(objStudent);

		} else {
			return null;
		}
	}

	public long count() {
		return studentRepository.count();
	}

}
