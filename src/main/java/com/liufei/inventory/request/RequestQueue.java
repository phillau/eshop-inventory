package com.liufei.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class RequestQueue {
    private RequestQueue(){}

    private static class InnerRequestQueue{
        private static List<ArrayBlockingQueue<Request>> list = new ArrayList<>();
    }

    public static List<ArrayBlockingQueue<Request>> getInstance(){
        return InnerRequestQueue.list;
    }
}
