/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.lifehalf.liuchao.learn.entity.User;
import top.lifehalf.liuchao.learn.mapper.UserMapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/27 10:25
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
//        user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
        user.setAuthorities(grantedAuthorities(user.getRoles()));
        return user;
    }

    private List<GrantedAuthority> grantedAuthorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null && !"".equals(roles.trim())) {
            for (String role : roles.split(",")) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }

    public static void main(String[] args) {
        System.out.println(new String(Base64.getDecoder().decode(
                "eHh3dlpLWXpmWHo5Q3V6UCUyRjFkWmtRJTNEJTNEOllVVmJTMFRmTDM5OXdibzRkMlZheVElM0QlM0Q")));
    }

}
