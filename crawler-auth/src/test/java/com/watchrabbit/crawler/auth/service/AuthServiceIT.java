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
package com.watchrabbit.crawler.auth.service;

import com.watchrabbit.crawler.api.AuthData;
import com.watchrabbit.crawler.auth.ContextTestBase;
import com.watchrabbit.crawler.auth.repository.AuthDataRepository;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class AuthServiceIT extends ContextTestBase {

    @Autowired
    AuthDataRepository authDataRepository;

    @Autowired
    AuthService authService;

    @Autowired
    RemoteWebDriverFactory firefoxFactory;

    @Test
    public void shouldLogInIntoAngularApp() {
        authService.addNewAuthData(new AuthData.Builder()
                .withDomain("api.watchrabbit.com")
                .withLogin("mariusz.luciow@gmail.com")
                .withPassword("wkswks12")
                .withAuthEndpointUrl("https://api.watchrabbit.com/signin")
                .withSessionDuration(60)
                .build()
        );
        authService.getSession("api.watchrabbit.com");

        authService.getSession("api.watchrabbit.com");

    }

}
