/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/27 17:37
 */
public class MyWebAuthenticationDetails extends WebAuthenticationDetails {

    private boolean imageCodeRight;

    public boolean isImageCodeRight() {
        return imageCodeRight;
    }

    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String imageCode = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String savedImageCode = (String) session.getAttribute("captcha");
        if (!StringUtils.isEmpty(savedImageCode)) {
            session.removeAttribute("captcha");
            if (!StringUtils.isEmpty(imageCode) && imageCode.equals(savedImageCode)) {
                this.imageCodeRight = true;
            }
        }
    }
}
