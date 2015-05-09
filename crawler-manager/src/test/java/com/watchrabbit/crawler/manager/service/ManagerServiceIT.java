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
package com.watchrabbit.crawler.manager.service;

import static com.watchrabbit.commons.sleep.Sleep.sleep;
import com.watchrabbit.crawler.api.AuthData;
import com.watchrabbit.crawler.auth.service.AuthService;
import com.watchrabbit.crawler.manager.ContextTestBase;
import com.watchrabbit.crawler.manager.model.Address;
import com.watchrabbit.crawler.manager.repository.AddressRepository;
import com.watchrabbit.crawler.manager.util.InternetAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class ManagerServiceIT extends ContextTestBase {

    @Autowired
    ManagerService managerService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AuthService authService;

    @Test
    public void shouldProcessTest() {
        authService.addNewAuthData(new AuthData.Builder()
                .withDomain("api.watchrabbit.com")
                .withLogin("mariusz.luciow@gmail.com")
                .withPassword("wkswks12")
                .withAuthEndpointUrl("https://api.watchrabbit.com/signin")
                .withSessionDuration(60)
                .build()
        );
        Address address = new Address.Builder()
                .withUrl("https://scalingapp.com")
                .withNextExecutionDate(new Date())
                .withDomainName(InternetAddress.getDomainName("https://scalingapp.com"))
                .build();
        addressRepository.save(address);
        managerService.findIdsForExecution(1000).forEach(managerService::orderExecution);
        sleep(10, TimeUnit.SECONDS);
        System.err.println("FIRST BATCH");
        addressRepository.findOrderByNextExecutionDate(1000).stream()
                .map(queued -> queued.getDomainName() + " " + queued.getUrl() + " " + queued.getNextExecutionDate())
                .sorted()
                .forEach(System.out::println);
        managerService.findIdsForExecution(1000).forEach(managerService::orderExecution);
        sleep(10, TimeUnit.SECONDS);
        System.err.println("SECOND BATCH");
        addressRepository.findOrderByNextExecutionDate(1000).stream()
                .map(queued -> queued.getDomainName() + " " + queued.getUrl() + " " + queued.getNextExecutionDate())
                .sorted()
                .forEach(System.out::println);
        managerService.findIdsForExecution(1000).forEach(managerService::orderExecution);
        sleep(10, TimeUnit.SECONDS);
        System.err.println("THIRD BATCH");
        addressRepository.findOrderByNextExecutionDate(1000).stream()
                .map(queued -> queued.getDomainName() + " " + queued.getUrl() + " " + queued.getNextExecutionDate())
                .sorted()
                .forEach(System.out::println);
        managerService.findIdsForExecution(1000).forEach(managerService::orderExecution);
        System.err.println("FOURTH BATCH");
        addressRepository.findOrderByNextExecutionDate(1000).stream()
                .map(queued -> queued.getDomainName() + " " + queued.getUrl() + " " + queued.getNextExecutionDate())
                .sorted()
                .forEach(System.out::println);
    }
}
