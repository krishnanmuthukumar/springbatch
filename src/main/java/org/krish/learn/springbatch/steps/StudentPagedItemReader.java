package org.krish.learn.springbatch.steps;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.krish.learn.springbatch.mapper.StudentMapper;
import org.krish.learn.springbatch.model.Student;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentPagedItemReader {

	@Autowired
	private DataSource dataSource;

	private static final String GET_STUDENT_INFO = "SELECT id, name, email from STUDENT";
	public StudentPagedItemReader() {
		System.out.println("getPaginationReader() called");
	}

	public JdbcPagingItemReader<Student> getPaginationReader() {
		System.out.println("getPaginationReader() called");
		final JdbcPagingItemReader<Student> reader = new JdbcPagingItemReader<>();
		final StudentMapper studentMapper = new StudentMapper();
		reader.setDataSource(dataSource);
		reader.setFetchSize(5);
		reader.setPageSize(5);
		reader.setRowMapper(studentMapper);
		reader.setQueryProvider(createQuery());
		Map<String, Object> parameters = new HashMap<>();
		reader.setParameterValues(parameters);
		return reader;
	}

	private PostgresPagingQueryProvider createQuery() {
		final Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);
		final PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
		queryProvider.setSelectClause("*");
		queryProvider.setFromClause(getFromClause());
		queryProvider.setSortKeys(sortKeys);
		System.out.println("createQuery");
		return queryProvider;
	}

	private String getFromClause() {
		return "( " + GET_STUDENT_INFO + ")" + " AS RESULT_TABLE ";
	}

}
