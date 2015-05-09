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
package com.watchrabbit.crawler.batch.facade;

import com.watchrabbit.crawler.api.ExecutionForm;
import com.watchrabbit.crawler.manager.service.ManagerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class LocalDispatcherServiceFacade implements DispatcherServiceFacade {

    @Autowired
    ManagerService managerService;

    @Override
    public List<String> getQueue(int limit) {
        return managerService.findIdsForExecution(limit);
    }

    @Override
    public void dispatch(ExecutionForm form) {
        managerService.orderExecution(form.getIds());
    }

}
