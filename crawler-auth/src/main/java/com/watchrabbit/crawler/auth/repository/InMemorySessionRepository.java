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
package com.watchrabbit.crawler.auth.repository;

import com.watchrabbit.commons.clock.Clock;
import com.watchrabbit.commons.clock.SystemClock;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.Cookie;

/**
 *
 * @author Mariusz
 */
public class InMemorySessionRepository implements SessionRepository {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    Clock clock = SystemClock.getInstance();

    @Override
    public Collection<Cookie> findByDomain(String domain) {
        Session session = sessions.get(domain);
        if (session != null) {
            if (clock.getDate().before(session.validTo)) {
                return session.cookies;
            } else {
                sessions.remove(domain);
            }
        }
        return null;
    }

    @Override
    public void save(String domain, Collection<Cookie> cookies, Date validTo) {
        sessions.put(domain, new Session(cookies, validTo));
    }

    private class Session {

        Collection<Cookie> cookies;

        Date validTo;

        public Session(Collection<Cookie> cookies, Date validTo) {
            this.cookies = cookies;
            this.validTo = validTo;
        }

    }
}
