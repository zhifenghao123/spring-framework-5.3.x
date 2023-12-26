package com.hao.spring.aspect2;

import org.springframework.stereotype.Service;

/**
 * OnlinePay class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@Service
public class OnlinePay implements IPay{
    @Override
    public void pay() {
        System.out.println("-------OnlinePay--------");
    }
}
