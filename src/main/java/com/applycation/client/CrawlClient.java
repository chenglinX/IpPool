package com.applycation.client;


import com.applycation.entity.Crawl;
import com.applycation.proxy.ProxyPool;
import com.applycation.crawler4j.CrawlerController;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * 这个类将爬虫的启动信息，封装成为了一个定时任务
 * */
@DisallowConcurrentExecution
public class CrawlClient implements StatefulJob {

    private static Logger logger = Logger.getLogger(CrawlClient.class);

    private static int count = 0;

    public static ProxyPool proxyPool = new ProxyPool();


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        synchronized (CrawlClient.class) {
            count++;
        }
        logger.info("#####第" + count + "次开始爬取#####");
        CrawlerController.fetchProxyIp();
        logger.info("#####爬取完毕#####");
    }
}
