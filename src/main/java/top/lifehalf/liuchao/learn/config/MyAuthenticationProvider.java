/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/27 16:11
 */
//@Component
public class MyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        if (usernamePasswordAuthenticationToken.getCredentials() == null) {
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider" +
                            ".badCredentials",
                    "密码不能为空"));
        } else {
            String password = usernamePasswordAuthenticationToken.getCredentials().toString();
            if (!password.equals(userDetails.getPassword())) {
                throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider" +
                                ".badCredentials",
                        "密码错误"));
            }
        }
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(s);
    }
}
