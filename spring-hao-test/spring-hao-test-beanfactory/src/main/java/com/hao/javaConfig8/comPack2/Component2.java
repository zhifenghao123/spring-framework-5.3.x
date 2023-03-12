package com.hao.javaConfig8.comPack2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

/**
 * Component2 class
 *
 * @author haozhifeng
 * @date 2023/03/09
 */
@Component
@ComponentScans({@ComponentScan("com.hao.javaConfig8.comPack2.comSubPack1"),
        @ComponentScan("com.hao.javaConfig8.comPack2.comSubPack2")})
public class Component2 {
}
