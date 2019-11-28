/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/27 14:05
 */
public class VerificationCodeException extends AuthenticationException {

    public VerificationCodeException() {
        super("图形验证码校验失败");
    }

}
