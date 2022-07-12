package org.krish.learn.springbatch.listener;

import org.krish.learn.springbatch.service.StudentService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class BatchJobExecutionListener extends JobExecutionListenerSupport {

	private StudentService studentService;

	public BatchJobExecutionListener(StudentService studentService) {
		this.studentService = studentService;
	}
	
	private Long totalcount;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("Batch Job Completed Successfully");
		}
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		totalcount= studentService.count();
		System.out.println("totalcount::"+totalcount);
		jobExecution.getExecutionContext().put("totalcount", totalcount);
		super.beforeJob(jobExecution);
	}
}
