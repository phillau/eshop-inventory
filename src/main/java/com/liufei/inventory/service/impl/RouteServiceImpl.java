package com.liufei.inventory.service.impl;

import com.liufei.inventory.request.Request;
import com.liufei.inventory.request.RequestQueue;
import com.liufei.inventory.request.impl.ProductInventoryRefreshRequest;
import com.liufei.inventory.request.impl.ProductInventoryUpdateRequest;
import com.liufei.inventory.service.RouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RouteServiceImpl implements RouteService {
    private static int queuesNum;
    @Value("${eshop.inventory.queues-number}")
    private int queuesNumTemp;

    @PostConstruct
    public void initQueuesNum() {
        queuesNum = queuesNumTemp;
    }

    private List<ArrayBlockingQueue<Request>> requestQueues = RequestQueue.getInstance();
    private Map<String, Boolean> flagMap = new ConcurrentHashMap<>();

    @Override
    public void route(Request request, boolean isForce) throws InterruptedException {
        int productId = request.getProductId();
        int index = productId & (queuesNum - 1);
        System.out.println("===========日志===========: 路由内存队列，商品id=" + productId + ", 队列索引=" + index);
        ArrayBlockingQueue<Request> requestQueue = requestQueues.get(index);
        //每次有新的更新数据库请求，都将该商品对应的flag重新置为true
        if (request instanceof ProductInventoryUpdateRequest) {
            flagMap.put(String.valueOf(productId), true);
        }
        if (request instanceof ProductInventoryRefreshRequest) {
            if (flagMap.get(productId) != null && flagMap.get(productId) == false && !isForce) {
                //如果为false，说明前面已经有更新对应商品缓存的操作了,并且isForce为false，说明不用强制刷新
                return;
            } else {
                //进入else，说明前面没有更新对应商品缓存的操作，或者isForce为true，那就将此更新缓存操作加入队列，并将此商品id对应的flag设置为false
                flagMap.put(String.valueOf(productId), false);
            }
        }
        requestQueue.put(request);
    }
}
