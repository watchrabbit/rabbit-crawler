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

import com.watchrabbit.commons.clock.Clock;
import com.watchrabbit.commons.clock.SystemClock;
import com.watchrabbit.crawler.api.LinkDto;
import com.watchrabbit.crawler.manager.model.Address;
import com.watchrabbit.crawler.manager.repository.AddressRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class DefaultCleanupPolicy implements CleanupPolicy {

    private final Set<BlacklistElement> blacklist = new HashSet<>();

    private final Map<BlacklistElement, Date> withWarning = new ConcurrentHashMap<>();

    Clock clock = SystemClock.getInstance();

    @Autowired
    AddressRepository addressRepository;

    @Override
    public void onError(Address address) {
        BlacklistElement blacklistElement = new BlacklistElement(address.getUrl(), address.getKeyword());
        if (withWarning.containsKey(blacklistElement)) {
            if (withWarning.get(blacklistElement).after(clock.getDate())) {
                withWarning.remove(blacklistElement);
                blacklist.add(blacklistElement);
            } else {
                withWarning.remove(blacklistElement);
            }
        } else {
            putOnQueueEnd(address);
            Calendar calendar = clock.getCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            withWarning.put(blacklistElement, calendar.getTime());
        }
    }

    @Override
    public boolean isOnBlacklist(LinkDto linkDto) {
        return blacklist.contains(new BlacklistElement(linkDto.getUrl(), linkDto.getKeyword()));
    }

    private void putOnQueueEnd(Address address) {
        addressRepository.findLastByNextExecutionDate()
                .ifPresent(last -> address.setNextExecutionDate(last.getNextExecutionDate()));
        addressRepository.save(address);
    }

    private class BlacklistElement {

        private final String url;

        private final String keyword;

        public BlacklistElement(String url, String keyword) {
            this.url = url;
            this.keyword = keyword;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + Objects.hashCode(this.url);
            hash = 37 * hash + Objects.hashCode(this.keyword);
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
            final BlacklistElement other = (BlacklistElement) obj;
            if (!Objects.equals(this.url, other.url)) {
                return false;
            }
            if (!Objects.equals(this.keyword, other.keyword)) {
                return false;
            }
            return true;
        }

    }
}
