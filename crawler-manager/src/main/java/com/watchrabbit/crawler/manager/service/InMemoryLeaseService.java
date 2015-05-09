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
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mariusz
 */
public class InMemoryLeaseService implements LeaseService {

    Clock clock = SystemClock.getInstance();

    private final Map<String, Date> leases = new ConcurrentHashMap<>();

    @Override
    public void createLease(String key, int second) {
        Calendar calendar = clock.getCalendar();
        calendar.add(Calendar.SECOND, second);
        leases.put(key, calendar.getTime());
    }

    @Override
    public boolean hasLease(String key) {
        Date get = leases.get(key);
        if (get == null) {
            return false;
        }
        if (get.before(clock.getDate())) {
            leases.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public void removeLease(String key) {
        leases.remove(key);
    }

}
