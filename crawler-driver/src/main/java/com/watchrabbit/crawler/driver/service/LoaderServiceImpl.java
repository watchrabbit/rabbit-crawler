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
package com.watchrabbit.crawler.driver.service;

import com.watchrabbit.crawler.driver.util.LoadingStrategy;
import java.util.List;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class LoaderServiceImpl implements LoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoaderServiceImpl.class);

    @Autowired
    List<LoadingStrategy> loadingStrategies;

    @Override
    public void waitFor(RemoteWebDriver driver) {
        loadingStrategies.stream()
                .filter(strategy -> strategy.shouldWait(driver))
                .forEach(strategy -> waitUsing(strategy, driver));
    }

    private void waitUsing(LoadingStrategy strategy, RemoteWebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, strategy.getWaitTime());
        try {
            wait.until(strategy.hasFinishedProcessing());
        } catch (TimeoutException ex) {
            LOGGER.info("Timed out on {}", driver.getCurrentUrl());
        }
    }

}
