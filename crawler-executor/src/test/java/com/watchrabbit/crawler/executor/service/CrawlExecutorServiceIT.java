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
package com.watchrabbit.crawler.executor.service;

import com.watchrabbit.crawler.api.AuthData;
import com.watchrabbit.crawler.api.CrawlForm;
import com.watchrabbit.crawler.api.CrawlResult;
import com.watchrabbit.crawler.auth.service.AuthService;
import com.watchrabbit.crawler.executor.ContextTestBase;
import com.watchrabbit.crawler.executor.facade.ManagerServiceFacade;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class CrawlExecutorServiceIT extends ContextTestBase {

    @Autowired
    CrawlExecutorService crawlExecutorService;

    @Autowired
    AuthService authService;

    @Autowired
    ManagerServiceFacade managerServiceFacade;

    @Before
    public void init() {
        reset(managerServiceFacade);
    }

    @Test
    public void shouldCollectLinks() {
        crawlExecutorService.processPage(new CrawlForm.Builder()
                .withDomain("scalingapp.com")
                .withUrl("https://scalingapp.com")
                .build()
        );

        ArgumentCaptor<CrawlResult> argumentCaptor = ArgumentCaptor.forClass(CrawlResult.class);
        verify(managerServiceFacade).consumeResult(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getLinks()).isNotEmpty();
    }

    @Test
    public void shouldLogInAndCollect() {
        authService.addNewAuthData(new AuthData.Builder()
                .withDomain("api.watchrabbit.com")
                .withLogin("mariusz.luciow@gmail.com")
                .withPassword("wkswks12")
                .withAuthEndpointUrl("https://api.watchrabbit.com/signin")
                .build()
        );

        crawlExecutorService.processPage(new CrawlForm.Builder()
                .withDomain("api.watchrabbit.com")
                .withUrl("https://api.watchrabbit.com/")
                .build()
        );

        ArgumentCaptor<CrawlResult> argumentCaptor = ArgumentCaptor.forClass(CrawlResult.class);
        verify(managerServiceFacade).consumeResult(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getLinks()).isNotEmpty();
    }
}
