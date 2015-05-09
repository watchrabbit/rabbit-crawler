package com.watchrabbit.crawler.batch;

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
import com.watchrabbit.crawler.manager.EnableManagerService;
import com.watchrabbit.crawler.manager.policy.LinkFilter;
import com.watchrabbit.crawler.manager.util.InternetAddress;
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
@EnableExecutorService
@EnableManagerService
@EnableBatchService
@WebIntegrationTest
@SpringBootApplication
@SpringApplicationConfiguration(classes = {ContextTestBase.class})
public class ContextTestBase {

    @Bean
    public LinkFilter linkFilter() {
        return (links) -> links.stream().filter(link -> InternetAddress.getDomainName(link).equals("scalingapp.com")
                || InternetAddress.getDomainName(link).equals("watchrabbit.com")
                || InternetAddress.getDomainName(link).equals("api.watchrabbit.com")).collect(toList());
    }

}
