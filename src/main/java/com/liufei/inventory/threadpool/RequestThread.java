package com.liufei.inventory.threadpool;

import com.liufei.inventory.request.Request;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

public class RequestThread implements Callable {
    private ArrayBlockingQueue<Request> queue;

    public RequestThread(ArrayBlockingQueue<Request> queue){
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        try{
            while (true){
                Request request = queue.take();
                System.out.println("===========日志===========: 工作线程处理请求，商品id=" + request.getProductId());
                request.process();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
