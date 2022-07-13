package org.krish.learn.springbatch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.krish.learn.springbatch.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper implements RowMapper<Student> {

	@Override
	public Student mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		System.out.println("student mapped invoked");
		Student student = new Student();
		student.setId(Integer.parseInt(rs.getString("id")));
		student.setName(rs.getString("name"));
		student.setEmail(rs.getString("email"));
		return student;
	}
}
