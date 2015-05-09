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

import com.watchrabbit.crawler.manager.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Mariusz
 */
public class DefaultEtiquettePolicy implements EtiquettePolicy {

    @Value("${crawler.manager.domainEtiquette:10}")
    int domainEtiquette;

    @Autowired
    LeaseService leaseService;

    @Override
    public boolean canProcessDomain(String domain) {
        return !leaseService.hasLease(domain);
    }

    @Override
    public void onDomainProcessing(String domain) {
        leaseService.createLease(domain, domainEtiquette);
    }

}
