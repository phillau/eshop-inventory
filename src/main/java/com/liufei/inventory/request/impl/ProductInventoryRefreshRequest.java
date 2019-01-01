package com.liufei.inventory.request.impl;

import  com.liufei.inventory.model.ProductInventory;
import com.liufei.inventory.request.Request;
import com.liufei.inventory.service.ProductInventoryService;

/**
 * 刷新商品库存缓存的请求，先从数据库获取对应商品，再将商品库存信息更新到redis缓存
 */
public class ProductInventoryRefreshRequest implements Request {
    private ProductInventory productInventory;
    private ProductInventoryService productInventoryService;

    public ProductInventoryRefreshRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        ProductInventory productInventoryFromDB = productInventoryService.findProductInventoryFromDB(productInventory);
        System.out.println("===========日志===========: 已查询到商品最新的库存数量，商品id=" + productInventoryFromDB.getProductId() + ", 商品库存数量=" + productInventoryFromDB.getInventoryNum());
        productInventoryService.setProductInventoryNumToCache(productInventoryFromDB);
    }

    @Override
    public int getProductId() {
        return productInventory.getProductId();
    }
}
