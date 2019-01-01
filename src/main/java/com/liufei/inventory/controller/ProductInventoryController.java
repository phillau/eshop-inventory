package com.liufei.inventory.controller;

import com.liufei.inventory.model.ProductInventory;
import com.liufei.inventory.request.impl.ProductInventoryRefreshRequest;
import com.liufei.inventory.request.impl.ProductInventoryUpdateRequest;
import com.liufei.inventory.service.ProductInventoryService;
import com.liufei.inventory.service.RouteService;
import com.liufei.inventory.vo.Response;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Controller
public class ProductInventoryController {
    @Resource
    private RouteService routeService;

    @Resource
    private ProductInventoryService productInventoryService;

    @RequestMapping("/updateProductInventoryNum")
    @ResponseBody
    public Response updateProductInventoryNum(@RequestBody ProductInventory productInventory) {
        System.out.println("===========日志===========: 接收到更新商品库存的请求，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryNum());
        ProductInventoryUpdateRequest request = new ProductInventoryUpdateRequest(productInventory, productInventoryService);
        try {
            routeService.route(request, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Response.failure("更新商品库存失败");
        }
        return Response.success("更新商品库存成功");
    }

    @RequestMapping("/findProductInventoryNumById")
    @ResponseBody
    public Response findProductInventoryNumById(Integer productId) {
        System.out.println("===========日志===========: 接收到一个商品库存的读请求，商品id=" + productId);
        if (productId == null) {
            return Response.failure("商品不存在！");
        }
        ProductInventory productInventory = new ProductInventory(productId);
        //首先从缓存中读取数据，如果缓存中存在，直接返回
        ProductInventory productInventoryFromCache = productInventoryService.findProductInventoryFromCache(productInventory);
        if (null != productInventoryFromCache && productInventoryFromCache.getInventoryNum() >= 0) {
            System.out.println("===========日志===========: 直接读取到了redis中的库存缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryNum());
            return Response.success(String.valueOf(productInventoryFromCache.getInventoryNum()));
        }
        //如果缓存中不存在的话，将更新缓存的操作加入内存队列
        long startTime = System.currentTimeMillis();
        try {
            routeService.route(new ProductInventoryRefreshRequest(productInventory, productInventoryService), false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Response.failure("商品数量获取失败，请重试！");
        }
        //循环判断缓存是否已经存在，如果200毫秒以后缓存中还没有数据，则直接从数据库中获取
        while (true) {
            if (System.currentTimeMillis() - startTime >= 200) {
                break;
            }
            productInventoryFromCache = productInventoryService.findProductInventoryFromCache(productInventory);
            if (null != productInventoryFromCache && productInventoryFromCache.getInventoryNum() >= 0) {
                System.out.println("===========日志===========: 在200ms内读取到了redis中的库存缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryNum());
                return Response.success(String.valueOf(productInventoryFromCache.getInventoryNum()));
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ProductInventory productInventoryFromDB = productInventoryService.findProductInventoryFromDB(productInventory);
        if (null != productInventoryFromDB && productInventoryFromDB.getInventoryNum() >= 0) {
            //如果数据库中获取到数据，先将数据强制刷新到缓存再返回
            //个人认为加上这一句先刷新缓存更新，保证不用在等待内存队列中数据库更新操作完成之前所有到读请求都读数据库，造成数据库压力较大，此时数据库和缓存确实可能是不一致的，但是就算从数据库查出来返回给客户也是和此方案一样的信息
            productInventoryService.setProductInventoryNumToCache(productInventoryFromDB);
            try {
                routeService.route(new ProductInventoryRefreshRequest(productInventory, productInventoryService), true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Response.success(String.valueOf(productInventoryFromDB.getInventoryNum()));
        } else {
            //如果数据库中也获取不到数据说明商品不存在，直接返回失败信息
            return Response.failure("商品不存在！");
        }
    }
}
