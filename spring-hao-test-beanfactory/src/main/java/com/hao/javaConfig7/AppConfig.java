package com.hao.javaConfig7;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
@Import(MyImportBeanDefinitionRegister.class)
public class AppConfig {
}
