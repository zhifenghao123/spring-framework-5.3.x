package com.hao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hao.mapper.OrderMapper;

/**
 * OrderService class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
@Component
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public String getOrder() {
        return orderMapper.getOrder();
    }
}
