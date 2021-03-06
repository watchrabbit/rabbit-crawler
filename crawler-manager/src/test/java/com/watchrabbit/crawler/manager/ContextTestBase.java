package com.watchrabbit.crawler.manager;

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
import com.watchrabbit.crawler.auth.EnableAuthService;
import com.watchrabbit.crawler.executor.EnableExecutorService;
import com.watchrabbit.crawler.executor.facade.ManagerServiceFacade;
import com.watchrabbit.crawler.manager.facade.LocalManagerServiceFacade;
import com.watchrabbit.crawler.manager.policy.LinkFilter;
import static java.util.stream.Collectors.toList;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Mariusz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@EnableAuthService
@WebIntegrationTest
@EnableExecutorService
@SpringBootApplication
@SpringApplicationConfiguration(classes = {ContextTestBase.class})
public class ContextTestBase {

    @Bean
    public ManagerServiceFacade managerServiceFacade() {
        return new LocalManagerServiceFacade();
    }

    @Bean
    public LinkFilter linkFilter() {
        return (links) -> links.stream().filter(link -> link.getUrl().contains("scalingapp") || link.getUrl().contains("watchrabbit")).collect(toList());
    }

}
