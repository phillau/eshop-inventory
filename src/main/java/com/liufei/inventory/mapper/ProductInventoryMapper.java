package com.liufei.inventory.mapper;

import com.liufei.inventory.model.ProductInventory;

public interface ProductInventoryMapper {
    boolean updateProductInventoryNum(ProductInventory productInventory);

    ProductInventory findProductInventoryById(ProductInventory productInventory);
}
