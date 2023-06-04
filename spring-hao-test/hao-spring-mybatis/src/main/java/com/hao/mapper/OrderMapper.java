package com.hao.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * OrderMapper class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
public interface OrderMapper {
    @Select("select 'order'")
    public String getOrder();
}
