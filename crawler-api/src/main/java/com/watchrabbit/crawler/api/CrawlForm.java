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
package com.watchrabbit.crawler.api;

/**
 *
 * @author Mariusz
 */
public class CrawlForm {

    private String domain;

    private String url;

    private String id;

    private boolean gateway;

    private String keyword;

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class Builder {

        private final CrawlForm item;

        public Builder() {
            this.item = new CrawlForm();
        }

        public Builder withDomain(final String domain) {
            this.item.domain = domain;
            return this;
        }

        public Builder withUrl(final String url) {
            this.item.url = url;
            return this;
        }

        public Builder withId(final String id) {
            this.item.id = id;
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

        public CrawlForm build() {
            return this.item;
        }
    }

}
