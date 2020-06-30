package com.pattabhi.batch;

import com.pattabhi.model.Address;
import com.pattabhi.model.User;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Value("${output.file.path}")
    private String extractionFilePath;

    @Value("${cron.scheduler}")
    private String cronScheduler;

    @Scheduled(cron = "${cron.scheduler}")
    public void extractUserDetails() throws Exception
    {
        System.out.println(" Job Started at :"+ new Date());
        JobParameters param = new JobParametersBuilder().addString("JobID",
                String.valueOf(System.currentTimeMillis())).toJobParameters();
        JobExecution execution = jobLauncher.run(exportUserJob(), param);
        System.out.println("Job finished with status :" + execution.getStatus());
    }

    @Bean
    public JdbcCursorItemReader<User> reader() {
        JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
        reader.setDataSource(dataSource);
        reader.setSql("select U.NAME name, U.DATE_OF_BIRTH dob, U.ADDRESS_EMAIL email,A.CITY city ,A.PINCODE pincode FROM USERS as U,ADDRESS A WHERE U.ADDRESS_EMAIL=A.EMAIL");
        reader.setRowMapper(new UserRowMapper());
        return reader;
    }

    public class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setDateOfBirth(rs.getString("dob"));
            user.setEmail(rs.getString("email"));
            Address address = new Address();
            address.setCity(rs.getString("city"));
            address.setPincode(rs.getString("pincode"));
            user.setAddress(address);
            return user;
        }

    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<User> writer() {
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        Resource resource = new FileSystemResource(this.extractionFilePath);
        writer.setResource(resource);
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<User>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<User>() {{
                setNames(new String[]{"name", "dateOfBirth","email","address.city","address.pincode"});
            }});
        }});

        return writer;
    }


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<User, User>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job exportUserJob() {
        return jobBuilderFactory.get("exportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

}