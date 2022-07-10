package org.krish.learn.springbatch.steps;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<String> {

	private String[] messages = { "Hello World", "Spring boot app with Spring Batch",
			"The is application uses h2 database", "Test message 4", "Test message 5", "Test message 6"

	};

	private int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (count < messages.length) {
			System.out.println("count::" + count);
			return messages[count++];
		} else {
			count = 0;
		}
		return null;
	}

}
