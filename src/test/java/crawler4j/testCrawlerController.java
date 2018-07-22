package crawler4j;

import com.applycation.crawler4j.CrawlerController;

import org.apache.log4j.Logger;


public class testCrawlerController {
    private static Logger logger = Logger.getLogger(testCrawlerController.class);
    @org.junit.Test
    public void testController(){
     CrawlerController.fetchProxyIp();
    }
}
