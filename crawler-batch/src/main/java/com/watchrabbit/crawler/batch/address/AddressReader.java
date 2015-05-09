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

import com.watchrabbit.crawler.batch.facade.DispatcherServiceFacade;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Mariusz
 */
public class AddressReader implements ItemReader<String>, ItemStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressReader.class);

    @Value("${crawler.manager.maxQueueProcessingSize:1000}")
    int maxQueueProcessingSize;

    @Autowired
    DispatcherServiceFacade dispatcherServiceFacade;

    List<String> ids;

    int index;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.ids = dispatcherServiceFacade.getQueue(maxQueueProcessingSize);
        this.index = 0;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        LOGGER.debug("Updating stream");
    }

    @Override
    public void close() throws ItemStreamException {
        LOGGER.debug("Closing stream");
        ids = null;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return ids.size() > index ? ids.get(index++) : null;
    }

}
