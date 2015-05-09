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
package com.watchrabbit.crawler.manager.repository;

import com.watchrabbit.crawler.manager.model.Address;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Mariusz
 */
public class InMemoryAddressRepository implements AddressRepository {

    private final Map<String, Address> addressesById = new ConcurrentHashMap<>();

    private final Map<String, Address> addressesByAddress = new ConcurrentHashMap<>();

    @Override
    public Address find(String id) {
        return addressesById.get(id);
    }

    @Override
    public Address findByUrl(String url) {
        return addressesByAddress.get(url);
    }

    @Override
    public void save(Address address) {
        if (address.getId() == null) {
            address.setId(UUID.randomUUID().toString());
        }
        addressesById.put(address.getId(), address);
        addressesByAddress.put(address.getUrl(), address);
    }

    @Override
    public List<Address> findOrderByNextExecutionDate(int limit) {
        return addressesById.values().stream()
                .sorted((first, second) -> second.getNextExecutionDate().compareTo(first.getNextExecutionDate()))
                .limit(limit)
                .collect(toList());
    }

}
