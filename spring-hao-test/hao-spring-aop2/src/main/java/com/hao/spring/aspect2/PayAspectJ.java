package com.hao.spring.aspect2;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * PayAspectJ class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
@Aspect
@Component
public class PayAspectJ {
    @DeclareParents(value = "com.hao.spring.aspect2.IPay+",defaultImpl = AlipayPlugin.class)
    public IPayPlugin alipayPlugin;
}
