/*
 * Copyright 2015 Mariusz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.watchrabbit.crawler.batch.config;

import com.watchrabbit.commons.marker.Todo;
import com.watchrabbit.crawler.batch.address.AddressReader;
import com.watchrabbit.crawler.batch.address.AddressWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Mariusz
 */
@Configuration
@Todo("Execute steps in pararel")
@Todo("Some embedded db cleaning")
@EnableBatchProcessing
public class AddressBatchConfig {

    @Value("${crawler.manager.chunkSize:3}")
    int chunkSize;

    @Autowired
    JobBuilderFactory jobs;

    @Autowired
    StepBuilderFactory steps;

    @Bean
    public Job ruleJob() throws Exception {
        return jobs.get("addressJob")
                .start(createRuleJobStep())
                .build();
    }

    @Bean
    public Step createRuleJobStep() {
        return steps.get("createAddressJobStep")
                .<String, String>chunk(chunkSize)
                .reader(addressReader())
                .writer(addressWriter())
                .build();
    }

    @Bean
    public AddressReader addressReader() {
        return new AddressReader();
    }

    @Bean
    public AddressWriter addressWriter() {
        return new AddressWriter();
    }
}
