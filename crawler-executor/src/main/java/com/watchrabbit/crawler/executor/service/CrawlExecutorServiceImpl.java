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

import com.watchrabbit.commons.clock.Stopwatch;
import com.watchrabbit.crawler.api.CrawlForm;
import com.watchrabbit.crawler.api.CrawlResult;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import com.watchrabbit.crawler.driver.service.LoaderService;
import com.watchrabbit.crawler.executor.facade.AuthServiceFacade;
import com.watchrabbit.crawler.executor.facade.ManagerServiceFacade;
import com.watchrabbit.crawler.executor.listener.CrawlListener;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.util.stream.Collectors.toList;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class CrawlExecutorServiceImpl implements CrawlExecutorService {

    @Autowired
    AuthServiceFacade authServiceFacade;

    @Autowired
    RemoteWebDriverFactory remoteWebDriverFactory;

    @Autowired
    ManagerServiceFacade managerServiceFacade;

    @Autowired
    LoaderService loaderService;

    @Autowired(required = false)
    CrawlListener crawlListener = driver -> 0;

    @Override
    public void processPage(CrawlForm form) {
        Collection<Cookie> session = authServiceFacade.getSession(form.getDomain());
        RemoteWebDriver driver = remoteWebDriverFactory.produceDriver();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted(() -> enableSession(driver, form.getUrl(), session));

            List<String> links = collectLinks(driver);
            int importanceFactor = crawlListener.accept(driver);
            managerServiceFacade.consumeResult(new CrawlResult.Builder()
                    .withDomain(form.getDomain())
                    .withMiliseconds(stopwatch.getExecutionTime(TimeUnit.MILLISECONDS))
                    .withUrl(form.getUrl())
                    .withLinks(links)
                    .withId(form.getId())
                    .withImportanceFactor(importanceFactor)
                    .build()
            );
        } finally {
            remoteWebDriverFactory.returnWebDriver(driver);
        }
    }

    private void enableSession(RemoteWebDriver driver, String url, Collection<Cookie> session) {
        driver.get(url);
        loaderService.waitFor(driver);
        if (!session.isEmpty()) {
            driver.manage().deleteAllCookies();
            session.forEach(driver.manage()::addCookie);

            driver.get(url);
            loaderService.waitFor(driver);
        }
    }

    private List<String> collectLinks(RemoteWebDriver driver) {
        return driver.findElements(By.xpath("//a")).stream()
                .filter(element -> element.isDisplayed())
                .map(link -> link.getAttribute("href"))
                .filter(link -> link != null)
                .filter(link -> link.startsWith("http"))
                .distinct()
                .collect(toList());
    }
}
