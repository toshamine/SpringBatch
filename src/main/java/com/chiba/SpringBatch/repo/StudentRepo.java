package com.chiba.SpringBatch.repo;


import com.chiba.SpringBatch.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student,Long> {
}
