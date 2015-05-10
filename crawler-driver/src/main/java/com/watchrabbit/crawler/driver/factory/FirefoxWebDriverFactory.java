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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class FirefoxWebDriverFactory implements RemoteWebDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirefoxWebDriverFactory.class);

    private final Queue<RemoteWebDriver> drivers = new ConcurrentLinkedQueue<>();

    @Value("${crawler.driver.maxWaitingDriverSessions:5}")
    private int maxWaitingDriverSessions;

    @PreDestroy
    void cleanup() {
        drivers.forEach(driver -> driver.quit());
    }

    @Override
    public synchronized RemoteWebDriver produceDriver() {
        LOGGER.debug("Returning new driver");
        if (drivers.isEmpty()) {
            LOGGER.debug("Creating new driver");
            FirefoxProfile profile = new FirefoxProfile();
            profile.setAcceptUntrustedCertificates(true);
            RemoteWebDriver ff = new FirefoxDriver(profile);
            return ff;
        } else {
            LOGGER.debug("Returning {} driver from pool", drivers.peek().getWindowHandle());
            return drivers.poll();
        }
    }

    @Override
    public void returnWebDriver(RemoteWebDriver driver) {
        if (driver != null) {
            LOGGER.debug("Moving {} driver back to pool", driver.getWindowHandle());
            if (drivers.size() > maxWaitingDriverSessions) {
                LOGGER.debug("Maximum waiting sessions exceeded, quitting driver");
                driver.quit();
            } else {
                driver.manage().deleteAllCookies();
                drivers.add(driver);
            }
        }
    }

}
