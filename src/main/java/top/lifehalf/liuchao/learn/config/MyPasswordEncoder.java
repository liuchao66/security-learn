/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/26 18:00
 */
@Component
public class MyPasswordEncoder
//        implements PasswordEncoder
    extends BCryptPasswordEncoder {

    private static Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2[ayb]?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    @Override
    public String encode(CharSequence charSequence) {
//        return charSequence.toString();
        return super.encode(charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        if (BCRYPT_PATTERN.matcher(s).matches()) {
            return super.matches(charSequence, s);
        }
        return s.equals(charSequence.toString());
    }
}
