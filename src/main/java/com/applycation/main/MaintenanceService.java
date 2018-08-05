package com.applycation.main;

import com.applycation.client.CrawlClient;
import com.applycation.proxy.HttpProxy;
import com.applycation.proxy.ProxyPool;
import com.applycation.util.Executor;
import com.applycation.util.HttpStatus;
import com.applycation.util.ProxyIpCheck;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;


public class MaintenanceService implements StatefulJob {
    private static Logger logger = Logger.getLogger(MaintenanceService.class);

    ProxyPool proxyPool = null;
    private static int count = 0;
    private Integer countLock = 2;
    private static int COREPOOLSIZE = 20;
    private static int TASKFORIPNUM = 10;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        synchronized (countLock){
            count++;
        }
        try {
        proxyPool = CrawlClient.proxyPool;
        logger.info("爬虫ip池第" + count + "次开始测试");
        int idleNum = proxyPool.getIdleNum();
        logger.info("idleNum:" + idleNum);
        int taskNum= idleNum  / TASKFORIPNUM;//size相当于是将这些ip分成多少组。

        countLock = taskNum;
        CountDownLatch countDownLatch = new CountDownLatch(countLock);

        ThreadPoolExecutor executor= Executor.newMyexecutor(COREPOOLSIZE);
        executor.allowCoreThreadTimeOut(true);
        for (int j = 0; j < taskNum; j++) {
            A a = new A(j,countDownLatch);
            executor.execute(a);
            logger.info("线程池中现在的线程数目是："+executor.getPoolSize()+
                    ",  队列中正在等待执行的任务数量为："+
                    executor.getQueue().size());
        }

            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }finally {
            //executor.shutdown();
        }

        logger.info("爬虫ip池第" + count + "次测试结果");
        proxyPool.allProxyStatus();  // 可以获取 ProxyPool 中所有 Proxy 的当前状态
    }

    class A implements Runnable {

        int j;
        CountDownLatch latch;
        public A(int j,CountDownLatch latch) {

            this.j = j;
            this.latch = latch;
        }

        @Override
        public void run() {
            logger.info("多线程分片跑区间:" + j*TASKFORIPNUM+ "-" + (j*TASKFORIPNUM+TASKFORIPNUM)+"启动>>>>>");
            for (int i = 0; i < TASKFORIPNUM; i++) {
                HttpProxy httpProxy = proxyPool.borrow();
                HttpStatus code = ProxyIpCheck.Check(httpProxy.getProxy());
                logger.info("name:" + Thread.currentThread().getName() + httpProxy.getProxy() + ":" + code);

                proxyPool.reback(httpProxy,code); // 使用完成之后，归还 Proxy,并将请求结果的 http 状态码一起传入
            }
            latch.countDown();

            logger.info("当前线程" + Thread.currentThread().getName() + "执行完毕:");
        }
    }
}
