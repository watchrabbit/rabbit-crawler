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

import com.google.common.collect.Lists;
import com.watchrabbit.crawler.batch.facade.DispatcherServiceFacade;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class TaskScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduler.class);

    @Value("${crawler.batch.maxQueueProcessingSize:1000}")
    int maxQueueProcessingSize;

    @Autowired
    DispatcherServiceFacade dispatcherServiceFacade;

    @Scheduled(initialDelayString = "${crawler.batch.delay:10000}")
    public void execute() {
        try {
            LOGGER.info("Starting address job");

            List<String> queue = dispatcherServiceFacade.getQueue(maxQueueProcessingSize);
            if (queue.size() > 0) {
                List<List<String>> partition = Lists.partition(queue, queue.size() / dispatcherServiceFacade.getInstanceCount());
                partition.forEach(dispatcherServiceFacade::dispatch);
            }
        } catch (RuntimeException e) {
            LOGGER.error("Address job exception", e);
        }
    }
}
