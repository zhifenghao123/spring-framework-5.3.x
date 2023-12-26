package com.hao.spring.aspect2;

/**
 * AlipayPlugin class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
public class AlipayPlugin implements IPayPlugin{
    @Override
    public void payPlugin() {
        System.out.println("-------Alipay--------");
    }
}
