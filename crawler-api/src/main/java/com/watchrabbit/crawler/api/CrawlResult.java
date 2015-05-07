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

import java.util.List;

/**
 *
 * @author Mariusz
 */
public class CrawlResult {

    private List<String> links;

    private long miliseconds;

    private String domain;

    private String url;

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public long getMiliseconds() {
        return miliseconds;
    }

    public void setMiliseconds(long miliseconds) {
        this.miliseconds = miliseconds;
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

        private final CrawlResult item;

        public Builder() {
            this.item = new CrawlResult();
        }

        public Builder withLinks(final List<String> links) {
            this.item.links = links;
            return this;
        }

        public Builder withMiliseconds(final long miliseconds) {
            this.item.miliseconds = miliseconds;
            return this;
        }

        public Builder withDomain(final String domain) {
            this.item.domain = domain;
            return this;
        }

        public Builder withUrl(final String url) {
            this.item.url = url;
            return this;
        }

        public CrawlResult build() {
            return this.item;
        }
    }

}
