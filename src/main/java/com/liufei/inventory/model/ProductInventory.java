package com.liufei.inventory.model;

public class ProductInventory {
    private int productId;
    private int inventoryNum;

    public ProductInventory(int productId) {
        this.productId = productId;
    }

    public ProductInventory() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getInventoryNum() {
        return inventoryNum;
    }

    public void setInventoryNum(int inventoryNum) {
        this.inventoryNum = inventoryNum;
    }
}
