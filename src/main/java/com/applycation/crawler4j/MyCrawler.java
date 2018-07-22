package com.applycation.crawler4j;

import com.applycation.client.CrawlClient;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.regex.Pattern;


/**
 * 自定义爬虫类需要继承WebCrawler类，决定哪些url可以被爬以及处理爬取的页面信息
 * @author
 *
 */

public class MyCrawler extends WebCrawler {

    private static Logger logger = Logger.getLogger(MyCrawler.class);
    /**
     * 正则匹配指定的后缀文件
     */

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");

    private static String page1 = "http://www.66ip.cn/";
   // private static String page2 = "http://www.mayidaili.com/free";


    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息
     * 第二个参数url封装了当前爬取的页面url信息
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches();
    }

    /**
     * 当我们爬到我们需要的页面，这个方法会被调用，我们可以尽情的处理这个页面
     * page参数封装了所有页面信息
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        //判断是不是html页面，如果是转换成String
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();


            try {
                Document doc = Jsoup.parse(html);
                if (page1.equals(url)) {
                    for (int i = 1; i < 9; i++) {
                        Elements trs = doc.select("table").get(2).select("tr");
                        Elements tds = trs.get(i).select("td");

                        String ip = tds.get(0).text();
                        int port = Integer.parseInt(tds.get(1).text());
                        logger.info(url+"#"+ip+":"+port);
                        String area = tds.get(2).text();
                        System.out.println("#"+ip+":"+port+"area"+area);
                        CrawlClient.proxyPool.add(ip, port);
                        //RedisOnMessageUtil.Push(area, ip, port);
                    }
                }
//                else if (page2.equals(url)) {
//                    for (int i = 1; i < 50; i++) {
//                        Elements trs = doc.select("table").select("tr");
//                        Elements tds = trs.get(i).select("td");
//
//                        String ip = tds.get(0).text();
//                        int port = Integer.parseInt(tds.get(1).text());
//                        logger.info(url+"#"+ip+":"+port);
//                        String area = tds.get(5).text();
//                        System.out.println("chenglinx:"+area);
//                       // CrawlClient.proxyPool.add(ip, port);
//
//                        //RedisOnMessageUtil.Push(area, ip, port);
//                    }
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
