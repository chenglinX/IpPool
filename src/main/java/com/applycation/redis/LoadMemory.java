package com.applycation.redis;

import com.applycation.client.CrawlClient;
import com.applycation.proxy.ProxyPool;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Iterator;
import java.util.Set;


public class LoadMemory implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Set<String> set = JedisUtils.getProxyIp();
        Iterator iterator = set.iterator();
        ProxyPool proxyPool = CrawlClient.proxyPool;
        while (iterator.hasNext()) {
            String proxyIp = iterator.next().toString().substring(8).split(":")[0];
            int proxyPort = Integer.valueOf(iterator.next().toString().substring(8).split(":")[1]);
            proxyPool.add(proxyIp, proxyPort);

        }

    }
}
