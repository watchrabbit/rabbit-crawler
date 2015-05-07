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

        public CrawlForm build() {
            return this.item;
        }
    }

}
