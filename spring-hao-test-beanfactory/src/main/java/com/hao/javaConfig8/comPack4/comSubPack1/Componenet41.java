package com.hao.javaConfig8.comPack4.comSubPack1;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.hao.javaConfig8.comPack4.condition.CustomerCondition1;
import com.hao.javaConfig8.comPack4.condition.CustomerCondition2;

/**
 * Componenet41 class
 *
 * @author haozhifeng
 * @date 2023/03/11
 */
@Component
@Conditional(value = {CustomerCondition1.class, CustomerCondition2.class})
public class Componenet41 {
}
