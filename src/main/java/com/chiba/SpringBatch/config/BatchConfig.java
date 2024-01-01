package com.chiba.SpringBatch.config;

import com.chiba.SpringBatch.config.processor.StudentProcessor;
import com.chiba.SpringBatch.dto.StudentDTO;
import com.chiba.SpringBatch.entity.Student;
import com.chiba.SpringBatch.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final StudentRepo studentRepo;

    @Bean
    public FlatFileItemReader<StudentDTO> itemReader(){
        FlatFileItemReader<StudentDTO> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/students.csv"));
        itemReader.setName("Student ItemReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public StudentProcessor studentProcessor(){
        return new StudentProcessor();
    }

    @Bean
    public RepositoryItemWriter<Student> write(){
        RepositoryItemWriter<Student> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(studentRepo);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    @Bean
    public Step importStep(){
        return new StepBuilder("csvImport",jobRepository)
                .<StudentDTO, Student>chunk(1000,platformTransactionManager)
                .reader(itemReader())
                .writer(write())
                .processor(studentProcessor())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob(){
        return new JobBuilder("importJob",jobRepository)
                .start(importStep())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
    public LineMapper<StudentDTO> lineMapper(){
        DefaultLineMapper<StudentDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","firstName","lastName");

        BeanWrapperFieldSetMapper<StudentDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentDTO.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
