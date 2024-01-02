package com.chiba.SpringBatch.controller;

import com.chiba.SpringBatch.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class ImportStudentController {

    private final StudentService studentService;


    @PostMapping
    public void importFromCsv(@RequestParam("file") MultipartFile file) throws IOException {
        studentService.importFromCsv(file);
    }
}
