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

import com.watchrabbit.commons.marker.Todo;
import com.watchrabbit.crawler.api.AuthData;
import com.watchrabbit.crawler.auth.repository.AuthDataRepository;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import com.watchrabbit.crawler.driver.service.LoaderService;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    AuthDataRepository authDataDao;

    @Autowired
    RemoteWebDriverFactory remoteWebDriverFactory;

    @Autowired
    LoaderService loaderService;

    @Override
    public void addNewAuthData(AuthData authData) {
        authDataDao.save(authData);
    }

    @Override
    @Todo("Store session for reuse")
    public Collection<Cookie> getSession(String domain) {
        RemoteWebDriver driver = remoteWebDriverFactory.produceDriver();
        try {
            AuthData authData = authDataDao.findByDomain(domain);
            if (authData == null) {
                LOGGER.info("Cannot find auth data for {}", domain);
                return emptyList();
            }
            driver.get(authData.getAuthEndpointUrl());
            loaderService.waitFor(driver);

            WebElement loginForm = locateLoginForm(driver);
            if (loginForm == null) {
                LOGGER.error("Cannot locate any form that matches criteria on {}", authData.getAuthEndpointUrl());
                return emptyList();
            }
            WebElement password = findPasswordInput(loginForm);
            WebElement login = findLoginInput(loginForm);

            login.sendKeys(authData.getLogin());
            password.sendKeys(authData.getPassword());
            loginForm.submit();
            loaderService.waitFor(driver);

            return driver.manage().getCookies();
        } finally {
            remoteWebDriverFactory.returnWebDriver(driver);
        }
    }

    private WebElement locateLoginForm(RemoteWebDriver driver) {
        for (WebElement form : driver.findElements(By.xpath("//form"))) {
            List<WebElement> inputs = form.findElements(By.xpath("//input")).stream()
                    .filter(input -> isLoginInput(input) || isPasswordInput(input))
                    .collect(toList());
            if (inputs.size() == 2) {
                return form;
            }
        }
        return null;
    }

    private boolean isLoginInput(WebElement input) {
        return input.getAttribute("type").equals("email") || input.getAttribute("type").equals("text");
    }

    private boolean isPasswordInput(WebElement input) {
        return input.getAttribute("type").equals("password");
    }

    private WebElement findLoginInput(WebElement loginForm) {
        try {
            return loginForm.findElement(By.xpath("//input[@type='email']"));
        } catch (NoSuchElementException ex) {
            return loginForm.findElement(By.xpath("//input[@type='text']"));
        }
    }

    private WebElement findPasswordInput(WebElement loginForm) {
        return loginForm.findElement(By.xpath("//input[@type='password']"));
    }

}
