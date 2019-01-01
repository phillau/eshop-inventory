package com.liufei.inventory.request.impl;

import com.liufei.inventory.model.ProductInventory;
import com.liufei.inventory.request.Request;
import com.liufei.inventory.service.ProductInventoryService;

public class ProductInventoryUpdateRequest implements Request {
    private ProductInventory productInventory;
    private ProductInventoryService productInventoryService;

    public ProductInventoryUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        System.out.println("===========日志===========: 数据库更新请求开始执行，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryNum());
        productInventoryService.removeProductInventoryNumFromCache(productInventory);
        // 为了模拟演示先删除了redis中的缓存，然后还没更新数据库的时候，读请求过来了，这里可以人工sleep一下
//		try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        productInventoryService.updateProductInventoryNumToDB(productInventory);
    }

    @Override
    public int getProductId() {
        return productInventory.getProductId();
    }
}
