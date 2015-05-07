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

import com.watchrabbit.crawler.driver.ContextTestBase;
import com.watchrabbit.crawler.driver.util.WaitFor;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Mariusz
 */
public class FirefoxWebDriverFactoryIT extends ContextTestBase {

    @Autowired
    @Qualifier("firefoxWebDriverFactory")
    RemoteWebDriverFactory firefoxFactory;

    @Test
    public void shouldGetDriver() {
        RemoteWebDriver driver = firefoxFactory.produceDriver();
        driver.get("http://google.com");

        assertThat(driver.getCurrentUrl()).contains("google");
        firefoxFactory.returnWebDriver(driver);
    }

    @Test
    public void shouldExtractCookie() {
        RemoteWebDriver driver = null;
        RemoteWebDriver driver2 = null;
        try {
            driver = firefoxFactory.produceDriver();
            driver.get("https://watchrabbit.com:4848/");

            driver2 = firefoxFactory.produceDriver();
            driver2.get("https://watchrabbit.com:4848/");

            driver2.manage().deleteAllCookies();
            driver2.manage().addCookie(driver.manage().getCookieNamed("JSESSIONID"));

            assertThat(driver.manage().getCookieNamed("JSESSIONID").getValue()).isEqualTo(driver2.manage().getCookieNamed("JSESSIONID").getValue());
        } finally {
            firefoxFactory.returnWebDriver(driver);
            firefoxFactory.returnWebDriver(driver2);
        }
    }

    @Test
    public void shouldWaitForLoad() {
        RemoteWebDriver driver = firefoxFactory.produceDriver();
        driver.get("https://scalingapp.com");
        WaitFor.load(driver);

        firefoxFactory.returnWebDriver(driver);
    }

    @Test
    public void shouldWaitForLoad2() {
        RemoteWebDriver driver = firefoxFactory.produceDriver();
        driver.get("https://api.watchrabbit.com/signin");

        WaitFor.load(driver);

        firefoxFactory.returnWebDriver(driver);
    }

}
