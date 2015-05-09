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
package com.watchrabbit.crawler.driver.factory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PreDestroy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class FirefoxWebDriverFactory implements RemoteWebDriverFactory {

    private final Queue<RemoteWebDriver> drivers = new ConcurrentLinkedQueue<>();

    @Value("${crawler.driver.maxWaitingDriverSessions:10}")
    private int maxWaitingDriverSessions;

    @PreDestroy
    void cleanup() {
        drivers.forEach(driver -> driver.quit());
    }

    @Override
    public synchronized RemoteWebDriver produceDriver() {
        if (drivers.isEmpty()) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setAcceptUntrustedCertificates(true);
            RemoteWebDriver ff = new FirefoxDriver(profile);
            return ff;
        } else {
            return drivers.poll();
        }
    }

    @Override
    public void returnWebDriver(RemoteWebDriver driver) {
        if (driver != null) {
            driver.manage().deleteAllCookies();
            if (drivers.size() > maxWaitingDriverSessions) {
                driver.quit();
            } else {
                driver.manage().deleteAllCookies();
                drivers.add(driver);
            }
        }
    }

}
