package org.krish.learn.springbatch.repository;

import org.krish.learn.springbatch.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
