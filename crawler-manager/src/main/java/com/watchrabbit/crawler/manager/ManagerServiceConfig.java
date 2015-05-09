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
package com.watchrabbit.crawler.manager;

import com.watchrabbit.crawler.executor.facade.ManagerServiceFacade;
import com.watchrabbit.crawler.executor.service.CrawlExecutorService;
import com.watchrabbit.crawler.manager.facade.ExecutorServiceFacade;
import com.watchrabbit.crawler.manager.facade.LocalExecutorServiceFacade;
import com.watchrabbit.crawler.manager.facade.LocalManagerServiceFacade;
import com.watchrabbit.crawler.manager.policy.BasicRevisitPolicy;
import com.watchrabbit.crawler.manager.policy.DefaultEtiquettePolicy;
import com.watchrabbit.crawler.manager.policy.EtiquettePolicy;
import com.watchrabbit.crawler.manager.policy.ImportancePolicy;
import com.watchrabbit.crawler.manager.policy.OPICImportancePolicy;
import com.watchrabbit.crawler.manager.policy.RevisitPolicy;
import com.watchrabbit.crawler.manager.repository.AddressOPICRepository;
import com.watchrabbit.crawler.manager.repository.AddressRepository;
import com.watchrabbit.crawler.manager.repository.InMemoryAddressOPICRepository;
import com.watchrabbit.crawler.manager.repository.InMemoryAddressRepository;
import com.watchrabbit.crawler.manager.service.InMemoryLeaseService;
import com.watchrabbit.crawler.manager.service.LeaseService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Mariusz
 */
@Configuration
@ComponentScan
public class ManagerServiceConfig {

    @Bean
    @ConditionalOnClass(ManagerServiceFacade.class)
    public ManagerServiceFacade managerServiceFacade() {
        return new LocalManagerServiceFacade();
    }

    @Bean
    @ConditionalOnClass(CrawlExecutorService.class)
    public ExecutorServiceFacade executorServiceFacade() {
        return new LocalExecutorServiceFacade();
    }

    @Bean
    @ConditionalOnMissingBean(LeaseService.class)
    public LeaseService leaseService() {
        return new InMemoryLeaseService();
    }

    @Bean
    @ConditionalOnMissingBean(EtiquettePolicy.class)
    public EtiquettePolicy etiquettePolicy() {
        return new DefaultEtiquettePolicy();
    }

    @Bean
    @ConditionalOnMissingBean(RevisitPolicy.class)
    public RevisitPolicy revisitPolicy() {
        return new BasicRevisitPolicy();
    }

    @Bean
    @ConditionalOnMissingBean(ImportancePolicy.class)
    public ImportancePolicy importancePolicy() {
        return new OPICImportancePolicy();
    }

    @Bean
    @ConditionalOnMissingBean(AddressRepository.class)
    public AddressRepository addressRepository() {
        return new InMemoryAddressRepository();
    }

    @Bean
    @ConditionalOnMissingBean(AddressOPICRepository.class)
    public AddressOPICRepository addressOPICRepository() {
        return new InMemoryAddressOPICRepository();
    }

}
