/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONPObject;
/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/26 17:31
 */
@RestController
@RequestMapping("/app/api")
public class AppController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/hello")
    public String hello() {
        return "hello, app";
    }

    @GetMapping(value = "/admin", produces = "application/javascript;charset=UTF-8")
    public String admin(String callback) {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        JSONPObject jsonpObject = new JSONPObject(callback);
        jsonpObject.addParameter(admin);
        return jsonpObject.toString();
    }

    @PostMapping("/users")
    public String users() {
        return "test user";
    }

}
