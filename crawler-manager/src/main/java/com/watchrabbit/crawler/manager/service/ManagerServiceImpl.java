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
package com.watchrabbit.crawler.manager.service;

import com.watchrabbit.commons.clock.Clock;
import com.watchrabbit.commons.clock.SystemClock;
import com.watchrabbit.crawler.api.CrawlForm;
import com.watchrabbit.crawler.api.CrawlResult;
import com.watchrabbit.crawler.manager.facade.ExecutorServiceFacade;
import com.watchrabbit.crawler.manager.model.Address;
import com.watchrabbit.crawler.manager.policy.CleanupPolicy;
import com.watchrabbit.crawler.manager.policy.EtiquettePolicy;
import com.watchrabbit.crawler.manager.policy.ImportancePolicy;
import com.watchrabbit.crawler.manager.policy.RevisitPolicy;
import com.watchrabbit.crawler.manager.repository.AddressRepository;
import com.watchrabbit.crawler.manager.util.InternetAddress;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerServiceImpl.class);

    Clock clock = SystemClock.getInstance();

    @Value("${crawler.manager.urlProcessingTimeout:3600}")
    int urlProcessingTimeout;

    @Value("${crawler.manager.queueTopResultsLimit:1000}")
    int queueTopResultsLimit;

    @Autowired
    ExecutorServiceFacade executorServiceFacade;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ImportancePolicy importancePolicy;

    @Autowired
    RevisitPolicy revisitPolicy;

    @Autowired
    LeaseService leaseService;

    @Autowired
    EtiquettePolicy etiquettePolicy;

    @Autowired
    CleanupPolicy cleanupPolicy;

    @Override
    public List<Address> findDiscoveredAddresses(int limit) {
        return addressRepository.findOrderByNextExecutionDate(limit);
    }

    @Override
    public void addPage(String url, boolean isGateway) {
        Address address = new Address.Builder()
                .withUrl(url)
                .withNextExecutionDate(new Date())
                .withGateway(isGateway)
                .withDomainName(InternetAddress.getDomainName(url))
                .build();
        addressRepository.save(address);
    }

    @Override
    public void onCrawlError(CrawlForm form) {
        Address address = addressRepository.find(form.getId());
        cleanupPolicy.onError(address);
    }

    @Override
    public void orderExecution(List<String> ids) {
        ids.forEach(this::orderExecution);
    }

    @Override
    public void orderExecution(String id) {
        Address address = addressRepository.find(id);
        LOGGER.info("Pushing {} to execution with keyword {}", address.getUrl(), address.getKeyword());
        etiquettePolicy.onDomainProcessing(address.getDomainName());
        leaseService.createLease(address.getId(), urlProcessingTimeout);
        executorServiceFacade.processPage(new CrawlForm.Builder()
                .withDomain(address.getDomainName())
                .withUrl(address.getUrl())
                .withId(id)
                .withGateway(address.isGateway())
                .withKeyword(address.getKeyword())
                .build()
        );
        putOnQueueEnd(address);
    }

    @Override
    public void onCrawlResult(CrawlResult result) {
        importancePolicy.processCrawlResult(result);
        double importance = importancePolicy.getImportance(result.getId());
        Date nextExecutionDate = revisitPolicy.getNextExecutionDate(importance);
        Address address = addressRepository.find(result.getId());
        address.setNextExecutionDate(nextExecutionDate);
        leaseService.removeLease(result.getId());
        addressRepository.save(address);
    }

    @Override
    public List<String> findIdsForExecution(int limit) {
        return addressRepository.findOrderByNextExecutionDate(queueTopResultsLimit).stream()
                .map(address -> new AddressWrapper(address))
                .distinct()
                .map(wrapper -> wrapper.address)
                .filter(address -> etiquettePolicy.canProcessDomain(address.getDomainName()))
                .filter(address -> !leaseService.hasLease(address.getId()))
                .map(adress -> adress.getId())
                .limit(limit)
                .collect(toList());
    }

    private void putOnQueueEnd(Address address) {
        addressRepository.findLastByNextExecutionDate()
                .ifPresent(last -> address.setNextExecutionDate(last.getNextExecutionDate()));
        addressRepository.save(address);
    }

    private class AddressWrapper {

        Address address;

        public AddressWrapper(Address address) {
            this.address = address;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.address.getDomainName());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AddressWrapper other = (AddressWrapper) obj;
            if (!Objects.equals(this.address.getDomainName(), other.address.getDomainName())) {
                return false;
            }
            return true;
        }

    }

}
