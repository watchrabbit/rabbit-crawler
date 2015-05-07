package com.watchrabbit.crawler.auth.repository;

import com.watchrabbit.crawler.auth.model.AuthData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mariusz
 */
public class InMemoryAuthDataRepository implements AuthDataRepository {

    private final Map<String, AuthData> authData = new ConcurrentHashMap<>();

    @Override
    public AuthData findByDomain(String domain) {
        return authData.get(domain);
    }

    @Override
    public void addNewAuthData(AuthData authData) {
        this.authData.put(authData.getDomain(), authData);
    }

}
