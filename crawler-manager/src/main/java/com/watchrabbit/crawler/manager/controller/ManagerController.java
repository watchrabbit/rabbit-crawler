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
package com.watchrabbit.crawler.manager.controller;

import com.watchrabbit.crawler.api.CrawlResult;
import com.watchrabbit.crawler.api.PageForm;
import com.watchrabbit.crawler.manager.service.ManagerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mariusz
 */
@RestController
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @RequestMapping(value = "/queue", method = RequestMethod.GET)
    public List<String> getQueue(@RequestParam(defaultValue = "1000") int limit) {
        return managerService.findIdsForExecution(limit);
    }

    @RequestMapping(value = "/dispatch", method = RequestMethod.POST)
    public void dispatch(@RequestBody List<String> ids) {
        managerService.orderExecution(ids);
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public void onResult(@RequestBody CrawlResult crawlResult) {
        managerService.onCrawlResult(crawlResult);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public void addPage(@RequestBody PageForm form) {
        managerService.addPage(form.getUrl());
    }
}
