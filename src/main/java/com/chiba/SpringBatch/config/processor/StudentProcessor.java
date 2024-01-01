package com.chiba.SpringBatch.config.processor;

import com.chiba.SpringBatch.dto.StudentDTO;
import com.chiba.SpringBatch.entity.Student;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class StudentProcessor implements ItemProcessor<StudentDTO, Student> {

    @Override
    public Student process(StudentDTO item) {
        Student student = Student.builder()
                .id(UUID.randomUUID().toString())
                .fullName(item.getFirstName()+" "+item.getLastName())
                .age(item.getAge())
                .build();
        return student;
    }


}
