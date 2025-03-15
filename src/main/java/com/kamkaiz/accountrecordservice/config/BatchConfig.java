package com.kamkaiz.accountrecordservice.config;

import com.kamkaiz.accountrecordservice.batch.AccountItemProcessor;
import com.kamkaiz.accountrecordservice.batch.AccountItemWriter;
import com.kamkaiz.accountrecordservice.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Configuration class for Spring Batch processing of Account data.
 * This class sets up the batch job infrastructure for importing account data from CSV files.
 *
 * Key responsibilities:
 * - Configures the batch job and its steps
 * - Sets up CSV file reader with appropriate line mapping
 * - Configures chunk-based processing for optimal performance
 * - Integrates processor and writer components
 *
 * The batch process follows this flow:
 * 1. Read accounts from CSV file
 * 2. Process each account (validation/transformation)
 * 3. Write accounts to database in chunks
 *
 * @see org.springframework.batch.core.Job
 * @see org.springframework.batch.core.Step
 * @see com.kamkaiz.accountrecordservice.batch.AccountItemProcessor
 * @see com.kamkaiz.accountrecordservice.batch.AccountItemWriter
 */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AccountItemWriter accountItemWriter;

    /**
     * Configures the main batch job for importing accounts.
     * The job uses a single step to process the account data.
     *
     * @return A configured Job instance for account import processing
     */
    @Bean
    public Job importAccountsJob() {
        return jobBuilderFactory.get("importAccountsJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    /**
     * Configures the main processing step of the batch job.
     * This step reads account data from a CSV file, processes it, and writes to the database.
     * Processing is done in chunks of 10 records for optimal performance.
     *
     * @return A configured Step instance for processing account data
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Account, Account>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(accountItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<Account> reader() {
        FlatFileItemReader<Account> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("accounts.txt"));
        reader.setLineMapper(lineMapper());
        reader.setLinesToSkip(1); // Skip header line if present
        return reader;
    }

    @Bean
    public LineMapper<Account> lineMapper() {
        DefaultLineMapper<Account> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|"); // Using pipe delimiter for transaction data
        tokenizer.setNames("accountNumber", "transactionAmount", "description", "transactionDate", "transactionTime", "customerId");
        tokenizer.setStrict(false); // Allow flexible number of columns

        BeanWrapperFieldSetMapper<Account> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Account.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public AccountItemProcessor processor() {
        return new AccountItemProcessor();
    }
}