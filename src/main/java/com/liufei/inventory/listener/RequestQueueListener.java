package com.liufei.inventory.listener;

import com.liufei.inventory.threadpool.RequestThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RequestQueueListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        RequestThreadPool.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
