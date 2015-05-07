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
package com.watchrabbit.crawler.driver.util;

import com.google.common.base.Predicate;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mariusz
 */
public class WaitFor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitFor.class);

    private static final String ANGULAR_GUARD = "return (window.angular != null) && "
            + "(window.angular.element(document.body).injector() != null) && "
            + "(window.angular.element(document.body).injector().get('$http').pendingRequests.length === 0)";

    public static void load(RemoteWebDriver driver) {
        if (isAngular(driver)) {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            try {
                wait.until(angularHasFinishedProcessing());
            } catch (TimeoutException ex) {
                LOGGER.info("Timed out on {}", driver.getCurrentUrl());
            }
        } else {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until((Predicate<WebDriver>) webDriver -> Boolean.FALSE);
        }
    }

    private static boolean isAngular(RemoteWebDriver driver) {
        return Boolean.valueOf(((JavascriptExecutor) driver).executeScript("return (window.angular != null)").toString());
    }

    private static Predicate<WebDriver> angularHasFinishedProcessing() {
        return driver
                -> Boolean.valueOf(((JavascriptExecutor) driver).executeScript(ANGULAR_GUARD).toString());
    }
}
