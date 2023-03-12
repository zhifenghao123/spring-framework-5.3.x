package com.hao.xml4.component;

import javax.annotation.Resource;

/**
 * Order class
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class Order {
    private String orderId;
    private String userAccountId;
    @Resource
    private Product product;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
