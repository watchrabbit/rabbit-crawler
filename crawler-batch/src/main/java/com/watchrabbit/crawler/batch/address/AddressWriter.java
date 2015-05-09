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
package com.watchrabbit.crawler.batch.address;

import com.watchrabbit.crawler.api.ExecutionForm;
import com.watchrabbit.crawler.batch.facade.DispatcherServiceFacade;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class AddressWriter implements ItemWriter<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressWriter.class);

    @Autowired
    DispatcherServiceFacade dispatcherServiceFacade;

    @Override
    public void write(List<? extends String> items) throws Exception {
        LOGGER.info("Processing addresses {}", items);
        ExecutionForm batch = new ExecutionForm();
        batch.setIds(items.stream()
                .map(something -> something.toString())
                .collect(toList())
        );
        dispatcherServiceFacade.dispatch(batch);
    }

}
