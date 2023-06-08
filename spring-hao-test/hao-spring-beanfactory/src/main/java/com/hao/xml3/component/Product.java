package com.hao.xml3.component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Product class
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class Product {
    private String productId;
    private String productName;
    private double price;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("in Product : @PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("in Product : @PreDestroy");
    }
}
