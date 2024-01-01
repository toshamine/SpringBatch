package com.chiba.SpringBatch.controller;

import com.chiba.SpringBatch.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class ImportStudentController {

    private final StudentService studentService;

    @PostMapping
    public void importFromCsv(){
        studentService.importFromCsv();
    }
}
