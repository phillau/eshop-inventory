package com.liufei.inventory.service;

import com.liufei.inventory.model.ProductInventory;

public interface ProductInventoryService {
    boolean updateProductInventoryNumToDB(ProductInventory productInventory);

    void removeProductInventoryNumFromCache(ProductInventory productInventory);

    ProductInventory findProductInventoryFromDB(ProductInventory productInventory);

    ProductInventory findProductInventoryFromCache(ProductInventory productInventory);

    void setProductInventoryNumToCache(ProductInventory productInventory);
}
