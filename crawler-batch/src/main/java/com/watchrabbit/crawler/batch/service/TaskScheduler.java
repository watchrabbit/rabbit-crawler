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
package com.watchrabbit.crawler.batch.service;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class TaskScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job addressJob;

    @Scheduled(fixedDelay = 10000)
    public void execute() {
        try {
            LOGGER.info("Starting address job");

            JobParameters params = new JobParametersBuilder()
                    .addDate("startDate", new Date())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(addressJob, params);

            if (execution.getStatus().isUnsuccessful()) {
                LOGGER.error("Address job failed");
            } else {
                LOGGER.info("Address job finished sucessfully");
            }
        } catch (JobExecutionException | RuntimeException e) {
            LOGGER.error("Rule job exception", e);
        }
    }
}
