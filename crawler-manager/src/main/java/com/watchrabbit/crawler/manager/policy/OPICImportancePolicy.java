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
package com.watchrabbit.crawler.manager.policy;

import com.watchrabbit.crawler.api.CrawlResult;
import com.watchrabbit.crawler.api.LinkDto;
import com.watchrabbit.crawler.manager.model.Address;
import com.watchrabbit.crawler.manager.model.AddressOPIC;
import com.watchrabbit.crawler.manager.repository.AddressOPICRepository;
import com.watchrabbit.crawler.manager.repository.AddressRepository;
import com.watchrabbit.crawler.manager.util.InternetAddress;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Mariusz
 */
public class OPICImportancePolicy implements ImportancePolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(OPICImportancePolicy.class);

    @Value("${crawler.manager.opicHistoricalResults:10}")
    int opicHistoricalResults;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    RevisitPolicy revisitPolicy;

    @Autowired
    CleanupPolicy cleanupPolicy;

    @Autowired
    AddressOPICRepository addressOPICRepository;

    @Autowired(required = false)
    LinkFilter linkFilter;

    @Override
    public void processCrawlResult(CrawlResult crawlResult) {
        AddressOPIC addressOPIC = addressOPICRepository.find(crawlResult.getId());
        if (addressOPIC == null) {
            addressOPIC = new AddressOPIC.Builder()
                    .withId(crawlResult.getId())
                    .build();
        }
        double cash = addressOPIC.getCash();
        List<LinkDto> links = crawlResult.getLinks();
        if (linkFilter != null) {
            links = linkFilter.filterLinks(links);
        }
        links = links.stream()
                .filter(link -> !cleanupPolicy.isOnBlacklist(link))
                .collect(toList());
        double change = cash / links.size();
        links.forEach(url -> distribute(url, change));
        addressOPIC.resetCash(opicHistoricalResults);
        addressOPIC.addCash(crawlResult.getImportanceFactor());
        addressOPICRepository.save(addressOPIC);
    }

    @Override
    public double getImportance(String id) {
        AddressOPIC addressOPIC = addressOPICRepository.find(id);
        if (addressOPIC == null) {
            LOGGER.debug("Cannot find opic metrics for {}. Creating new", id);
            addressOPIC = new AddressOPIC.Builder()
                    .withId(id)
                    .build();
            addressOPICRepository.save(addressOPIC);
            return addressOPIC.getImportance();
        }
        return addressOPIC.getImportance();
    }

    private void distribute(LinkDto link, double change) {
        Address address = addressRepository.findByUrlAndKeyword(link.getUrl(), link.getKeyword());
        if (address == null) {
            LOGGER.debug("Creating new address {} with keyword {}", link.getUrl(), link.getKeyword());
            Date nextExecutionDate = revisitPolicy.getNextExecutionDate(change);
            address = new Address.Builder()
                    .withNextExecutionDate(nextExecutionDate)
                    .withDomainName(InternetAddress.getDomainName(link.getUrl()))
                    .withUrl(link.getUrl())
                    .withKeyword(link.getKeyword())
                    .build();
            addressRepository.save(address);
        }
        AddressOPIC addressOPIC = addressOPICRepository.find(address.getId());
        if (addressOPIC == null) {
            addressOPIC = new AddressOPIC.Builder()
                    .withId(address.getId())
                    .build();
        }
        addressOPIC.addCash(change);
        addressOPICRepository.save(addressOPIC);
    }

}
