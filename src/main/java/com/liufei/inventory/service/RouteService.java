package com.liufei.inventory.service;

import com.liufei.inventory.request.Request;

public interface RouteService {
    void route(Request request,boolean isForce) throws InterruptedException;
}
