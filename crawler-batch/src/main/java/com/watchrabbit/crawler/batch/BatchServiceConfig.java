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
package com.watchrabbit.crawler.batch;

import com.watchrabbit.crawler.batch.facade.DispatcherServiceFacade;
import com.watchrabbit.crawler.batch.facade.LocalDispatcherServiceFacade;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Mariusz
 */
@Configuration
@EnableScheduling
@ComponentScan
public class BatchServiceConfig {

    @Bean
    @ConditionalOnClass(name = "com.watchrabbit.crawler.manager.service.ManagerService")
    public DispatcherServiceFacade dispatcherServiceFacade() {
        return new LocalDispatcherServiceFacade();
    }
}
