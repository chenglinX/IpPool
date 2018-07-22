package crawler4j;

import com.applycation.client.CrawlClient;
import com.applycation.crawler4j.CrawlerController;

import com.applycation.proxy.HttpProxy;
import com.applycation.proxy.ProxyPool;
import org.apache.log4j.Logger;

import java.util.Map;


public class TestCrawlerController {
    private static Logger logger = Logger.getLogger(TestCrawlerController.class);
    @org.junit.Test
    public void testController(){
     CrawlerController.fetchProxyIp();
     ProxyPool p = CrawlClient.proxyPool;

        System.out.println("chenglinx》》》》》》"+p.getIdleNum());
    }
}
