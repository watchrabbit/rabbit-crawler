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
package com.watchrabbit.crawler.executor.strategy;

import com.watchrabbit.crawler.api.CrawlForm;
import java.util.List;
import java.util.Random;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Mariusz
 */
public class PageContentWordsStrategy implements KeywordGenerateStrategy {

    @Value("${crawler.executor.maximumKeywords:10}")
    int maximumKeywords;

    @Override
    public List<String> generateKeywords(CrawlForm form, RemoteWebDriver driver) {
        String pageText = driver.findElement(By.tagName("body")).getText();
        List<String> words = Stream.of(pageText.split("\\s"))
                .map(wordWithPunctation -> wordWithPunctation.replaceAll("[^a-zA-Z]", ""))
                .map(mixCaseWord -> mixCaseWord.toLowerCase())
                .filter(anyWord -> anyWord.length() > 3 && anyWord.length() < 20)
                .collect(toList());

        Random random = new Random();
        return Stream.generate(() -> random.nextInt() % words.size())
                .map(index -> index < 0 ? index + words.size() : index)
                .map(index -> words.get(index))
                .distinct()
                .limit(maximumKeywords)
                .collect(toList());
    }

}
