/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.lifehalf.liuchao.learn.entity.User;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/27 10:23
 */
public interface UserMapper {

    @Select("select * from custom_user where username=#{username}")
    User findByUserName(@Param("username") String username);

}
