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

import com.watchrabbit.commons.marker.Feature;
import com.watchrabbit.commons.marker.Todo;
import com.watchrabbit.crawler.api.CrawlForm;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import com.watchrabbit.crawler.driver.util.WaitFor;
import com.watchrabbit.crawler.executor.facade.AuthServiceFacade;
import com.watchrabbit.crawler.executor.listener.CrawlListener;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.List;
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
    
    @Autowired(required = false)
    List<CrawlListener> crawlListeners = emptyList();
    
    @Override
    @Feature("Second load after setting cookies maybe not necessary")
    @Todo("send links to executor")
    public void processPage(CrawlForm form) {
        Collection<Cookie> session = authServiceFacade.getSession(form.getDomain());
        RemoteWebDriver driver = remoteWebDriverFactory.produceDriver();
        try {
            enableSession(driver, form.getUrl(), session);
            
            collectLinks(driver).forEach(System.out::println);
            crawlListeners.forEach(listener -> listener.accept(driver));
        } finally {
            remoteWebDriverFactory.returnWebDriver(driver);
        }
    }
    
    private void enableSession(RemoteWebDriver driver, String url, Collection<Cookie> session) {
        driver.get(url);
        WaitFor.load(driver);
        if (!session.isEmpty()) {
            driver.manage().deleteAllCookies();
            session.forEach(driver.manage()::addCookie);
            
            driver.get(url);
            WaitFor.load(driver);
        }
    }
    
    private List<String> collectLinks(RemoteWebDriver driver) {
        return driver.findElements(By.xpath("//a")).stream()
                .map(link -> link.getAttribute("href"))
                .filter(link -> link != null)
                .filter(link -> link.startsWith("http"))
                .distinct()
                .collect(toList());
    }
}
