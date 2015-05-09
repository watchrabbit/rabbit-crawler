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
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Mariusz
 */
public class BasicRevisitPolicy implements RevisitPolicy {

    Clock clock = SystemClock.getInstance();

    Random random = new Random();

    @Value("${crawler.manager.maximumIntervalBetweenCrawls:86400}")
    int maximumIntervalBetweenCrawls;

    @Value("${crawler.manager.minimumIntervalBetweenCrawls:300}")
    int minimumIntervalBetweenCrawls;

    @Value("${crawler.manager.distributionFactor:60}")
    int distributionFactor;

    @Override
    public Date getNextExecutionDate(double importance) {
        int secondsBonus = (int) importance * 1000;
        Calendar now = clock.getCalendar();
        now.add(Calendar.SECOND, maximumIntervalBetweenCrawls);
        now.add(Calendar.SECOND, -secondsBonus);
        Date nextExecution = now.getTime();
        if (getNextMinimumDate().after(nextExecution)) {
            nextExecution = getNextMinimumDate();
        }
        return new Date(nextExecution.getTime() + random.nextInt(distributionFactor) * 1000);
    }

    private Date getNextMinimumDate() {
        Calendar now = clock.getCalendar();
        now.add(Calendar.SECOND, minimumIntervalBetweenCrawls);
        return now.getTime();
    }

}
