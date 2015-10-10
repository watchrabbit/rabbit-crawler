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
import com.watchrabbit.crawler.api.LinkDto;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import com.watchrabbit.crawler.driver.service.LoaderService;
import com.watchrabbit.crawler.executor.facade.AuthServiceFacade;
import com.watchrabbit.crawler.executor.facade.ManagerServiceFacade;
import com.watchrabbit.crawler.executor.listener.CrawlListener;
import com.watchrabbit.crawler.executor.strategy.KeywordGenerateStrategy;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
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
public class CrawlExecutorServiceImpl implements CrawlExecutorService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlExecutorServiceImpl.class);
    
    @Autowired
    AuthServiceFacade authServiceFacade;
    
    @Autowired
    RemoteWebDriverFactory remoteWebDriverFactory;
    
    @Autowired
    ManagerServiceFacade managerServiceFacade;
    
    @Autowired
    LoaderService loaderService;
    
    @Autowired
    KeywordGenerateStrategy keywordGenerateStrategy;
    
    @Autowired(required = false)
    CrawlListener crawlListener = (pageId, driver) -> 0;
    
    @Override
    public void processPage(CrawlForm form) {
        Collection<Cookie> session = authServiceFacade.getSession(form.getDomain());
        RemoteWebDriver driver = remoteWebDriverFactory.produceDriver();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted(() -> enableSession(driver, form, session));
            LOGGER.debug("Finished loading {} in {}", form.getUrl(), stopwatch.getExecutionTime(TimeUnit.MILLISECONDS));
            
            List<LinkDto> links = collectLinks(driver).stream()
                    .map(link -> new LinkDto.Builder()
                            .withUrl(link)
                            .build()
                    ).collect(toList());
            if (form.isGateway()) {
                LOGGER.debug("Processing gateway {}", form.getUrl());
                List<String> keywords = keywordGenerateStrategy.generateKeywords(form, driver);
                links.addAll(
                        keywords.stream()
                        .map(keyword -> new LinkDto.Builder()
                                .withKeyword(keyword)
                                .withUrl(form.getUrl())
                                .build()
                        ).collect(toList())
                );
            }
            double importanceFactor = crawlListener.accept(form.getId(), driver);
            managerServiceFacade.consumeResult(new CrawlResult.Builder()
                    .withDomain(form.getDomain())
                    .withMiliseconds(stopwatch.getExecutionTime(TimeUnit.MILLISECONDS))
                    .withUrl(form.getUrl())
                    .withLinks(links)
                    .withId(form.getId())
                    .withImportanceFactor(importanceFactor)
                    .build()
            );
        } catch (Exception ex) {
            LOGGER.error("Execption on processing page " + form.getUrl(), ex);
            managerServiceFacade.onError(form);
        } finally {
            remoteWebDriverFactory.returnWebDriver(driver);
        }
    }
    
    private void enableSession(RemoteWebDriver driver, CrawlForm form, Collection<Cookie> session) {
        driver.get(form.getUrl());
        loaderService.waitFor(driver);
        if (!session.isEmpty()) {
            driver.manage().deleteAllCookies();
            session.forEach(driver.manage()::addCookie);
            
            driver.get(form.getUrl());
            loaderService.waitFor(driver);
        }
        if (StringUtils.isNotEmpty(form.getKeyword())) {
            Optional<SearchForm> searchFormOptional = findSearchInput(driver);
            searchFormOptional.ifPresent(searchForm -> {
                searchForm.input.sendKeys(form.getKeyword());
                loaderService.waitFor(driver);
                searchForm.submit.click();
                loaderService.waitFor(driver);
            });
            
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
    
    private Optional<SearchForm> findSearchInput(RemoteWebDriver driver) {
        for (WebElement form : driver.findElements(By.xpath("//form"))) {
            LOGGER.debug("Looking to form with action {}", form.getAttribute("action"));
            List<WebElement> inputs = form.findElements(By.xpath(".//input")).stream()
                    .filter(input -> input.getAttribute("type").equals("text"))
                    .filter(input -> input.isDisplayed())
                    .collect(toList());
            List<WebElement> passwords = form.findElements(By.xpath(".//input")).stream()
                    .filter(input -> input.getAttribute("type").equals("password"))
                    .filter(input -> input.isDisplayed())
                    .collect(toList());
            if (inputs.size() == 1 && passwords.isEmpty()) {
                List<WebElement> submit = form.findElements(By.xpath(".//button[@type='submit']"));
                if (submit.isEmpty()) {
                    submit = form.findElements(By.xpath(".//input[@type='submit']"));
                }
                if (!submit.isEmpty()) {
                    return Optional.of(new SearchForm(inputs.get(0), submit.get(0)));
                }
            }
        }
        LOGGER.error("Cannot find form in gateway page");
        return Optional.<SearchForm>empty();
    }
    
    private class SearchForm {
        
        WebElement input;
        
        WebElement submit;
        
        public SearchForm(WebElement input, WebElement submit) {
            this.input = input;
            this.submit = submit;
        }
        
    }
}
