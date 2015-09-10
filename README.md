[![Maven central][maven img]][maven]
[![][travis img]][travis]

Watchrabbit - Crawler
=====================

Distributed crawler with full JavaScript support, browsing hidden and private web.

## Powered by [watchrabbit.com]

## Current release
30/05/2015 rabbit-crawler **8.0.2** released! Should appear in maven central shortly.
 
 ## Running crawler
 
 To run crawler add depenencies to all modules of this application, and annotate your configuration class with `@EnableBatchService`, `@EnableAuthService`, `@EnableExecutorService` and `@EnableManagerService`.
 Then you need to implement `ManagerServiceFacade`
 ```
 @Service
 public class LocalManagerServiceFacade implements ManagerServiceFacade {

    @Autowired
    ManagerService managerService;

    @Override
    public void consumeResult(CrawlResult result) {
        managerService.onCrawlResult(result);
    }

    @Override
    public void onError(CrawlForm form) {
        managerService.onCrawlError(form);
    }

}
 ```
 If you want to perform aditional logic on processed pages implement `CrawlListener`.
 As a final step add pages you want to crawl using `ManagerService` service or via REST endpoint defined in `ManagerController`. If your's site require auth add account data using `AuthService` serice or `AuthController` endpoint.
 
 

[watchrabbit.com]:http://watchrabbit.com
[travis]:https://travis-ci.org/watchrabbit/rabbit-crawler
[travis img]:https://travis-ci.org/watchrabbit/rabbit-crawler.svg?branch=master
[maven]:https://maven-badges.herokuapp.com/maven-central/com.watchrabbit/rabbit-crawler
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.watchrabbit/rabbit-crawler/badge.svg
