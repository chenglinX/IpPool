package com.applycation.crawler4j;

import com.applycation.entity.Crawl;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.applycation.util.PageConfig.list;
/**
 * 这个类对爬虫信息进行封装，并启动了所有的爬虫
 * */
public class CrawlerController {
    private static Logger logger = Logger.getLogger(CrawlerController.class);
    public static void fetchProxyIp() {
        //chenglinx 当前文件的目录 nio中的用法；定义爬虫数据存储位置
        String crawlStorageFolder = Paths.get(".").toString();
        // 定义7个爬虫，也就是7个线程
        int numberOfCrawlers = 1;

        //chenglinx 从配置文件中的到需要爬取的网站的个数
        int size = list.size();
        //chenglinx 有多少个网站对应就有多少个爬虫
        List<Crawl> crawlList = new ArrayList(size);

        for (int i = 0 ; i < list.size() ; i++) {
            try {
                Crawl c = new Crawl();
                //chengx crawler4j 中的每个爬虫有一个Config；定义爬虫配置
                CrawlConfig config = new CrawlConfig();
                config.setMaxDepthOfCrawling(0);
                config.setPolitenessDelay(0);
                // 设置爬虫文件存储位置
                config.setCrawlStorageFolder(crawlStorageFolder + File.separator + "Controller"+i);
                c.setCrawlConfig(config);
                /*
                 * 实例化爬虫控制器
                 */
                // 实例化页面获取器
                PageFetcher pageFetcher = new PageFetcher(config);
                c.setPageFetcherer(pageFetcher);
                // 实例化爬虫机器人配置 比如可以设置 user-agent
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                // 实例化爬虫控制器
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
                /**
                 * 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
                 */
                controller.addSeed(list.get(i));
                c.setCrawlController(controller);
                c.setCrawlName("Controller"+i);
                crawlList.add(c);

            } catch (Exception e) {
            logger.error(e.getMessage());
            }
        }

        for (Crawl c : crawlList) {
            c.getCrawlController().start(MyCrawler.class, numberOfCrawlers);
            c.getCrawlController().waitUntilFinish();
            logger.info("Crawler "+c.getCrawlName()+" is finished.");
        }
    }
}
