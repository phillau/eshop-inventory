package com.liufei.inventory.service.impl;

import com.liufei.inventory.dao.RedisDAO;
import com.liufei.inventory.mapper.ProductInventoryMapper;
import com.liufei.inventory.model.ProductInventory;
import com.liufei.inventory.service.ProductInventoryService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Resource
    private ProductInventoryMapper productInventoryMapper;

    @Resource
    private RedisDAO redisDAO;

    @Override
    public boolean updateProductInventoryNumToDB(ProductInventory productInventory) {
        boolean b = productInventoryMapper.updateProductInventoryNum(productInventory);
        System.out.println("===========日志===========: 已修改数据库中的库存，flag="+b+" 商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryNum());
        return b;
    }

    @Override
    public void removeProductInventoryNumFromCache(ProductInventory productInventory) {
        String key = "product:inventory:"+productInventory.getProductId();
        redisDAO.delete(key);
        System.out.println("===========日志===========: 已删除redis中的缓存，key=" + key);
    }

    @Override
    public ProductInventory findProductInventoryFromDB(ProductInventory productInventory) {
        return productInventoryMapper.findProductInventoryById(productInventory);
    }

    @Override
    public ProductInventory findProductInventoryFromCache(ProductInventory productInventory) {
        String key = "product:inventory:"+productInventory.getProductId();
        String inventoryNum = redisDAO.get(key);
        try {
            productInventory.setInventoryNum(Integer.parseInt(inventoryNum));
        }catch (Exception e){
            productInventory.setInventoryNum(-1);
        }
        return productInventory;
    }

    @Override
    public void setProductInventoryNumToCache(ProductInventory productInventory) {
        String key = "product:inventory:"+productInventory.getProductId();
        redisDAO.set(key, String.valueOf(productInventory.getInventoryNum()));
        System.out.println("===========日志===========: 已更新商品库存的缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryNum() + ", key=" + key);
    }
}
