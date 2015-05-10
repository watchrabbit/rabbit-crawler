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
package com.watchrabbit.crawler.manager.model;

import java.util.Date;

/**
 *
 * @author Mariusz
 */
public class Address {

    private String id;

    private String url;

    private boolean gateway;

    private String keyword;

    private String domainName;

    private Date nextExecutionDate;

    public boolean isGateway() {
        return gateway;
    }

    public void setGateway(boolean gateway) {
        this.gateway = gateway;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Date getNextExecutionDate() {
        return nextExecutionDate;
    }

    public void setNextExecutionDate(Date nextExecutionDate) {
        this.nextExecutionDate = nextExecutionDate;
    }

    public static class Builder {

        private final Address item;

        public Builder() {
            this.item = new Address();
        }

        public Builder withId(final String id) {
            this.item.id = id;
            return this;
        }

        public Builder withUrl(final String url) {
            this.item.url = url;
            return this;
        }

        public Builder withGateway(final boolean gateway) {
            this.item.gateway = gateway;
            return this;
        }

        public Builder withKeyword(final String keyword) {
            this.item.keyword = keyword;
            return this;
        }

        public Builder withDomainName(final String domainName) {
            this.item.domainName = domainName;
            return this;
        }

        public Builder withNextExecutionDate(final Date nextExecutionDate) {
            this.item.nextExecutionDate = nextExecutionDate;
            return this;
        }

        public Address build() {
            return this.item;
        }
    }

}
