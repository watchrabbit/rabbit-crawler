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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mariusz
 */
public class AddressOPIC {

    private String id;

    private List<Double> history = new ArrayList<>();

    private double cash = 1;

    public double getImportance() {
        return history.stream().reduce((double) 0, (first, second) -> first + second) + cash;
    }

    public void resetCash(int historicalResults) {
        if (history.size() > historicalResults) {
            history.remove(0);
        }
        history.add(cash);
        cash = 1;
    }

    public void addCash(double change) {
        cash += change;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getHistory() {
        return history;
    }

    public void setHistory(List<Double> history) {
        this.history = history;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public static class Builder {

        private final AddressOPIC item;

        public Builder() {
            this.item = new AddressOPIC();
        }

        public Builder withId(final String id) {
            this.item.id = id;
            return this;
        }

        public Builder withHistory(final List<Double> history) {
            this.item.history = history;
            return this;
        }

        public Builder withCash(final double cash) {
            this.item.cash = cash;
            return this;
        }

        public AddressOPIC build() {
            return this.item;
        }
    }

}
