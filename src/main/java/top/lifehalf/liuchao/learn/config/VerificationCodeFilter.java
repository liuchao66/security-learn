/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/27 14:07
 */
public class VerificationCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler = (HttpServletRequest httpServletRequest,
                                                                         HttpServletResponse httpServletResponse, AuthenticationException e) -> {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(401);
        PrintWriter out = httpServletResponse.getWriter();
        out.write("{\"error_code\":\"401\", \"name\":\"" + e.getClass() + "\", \"message\":\"" + e.getMessage() +  "\"}");
    };


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!"/auth/form".equals(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            try {
                verificationCode(httpServletRequest);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (VerificationCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }
    }

    public void verificationCode(HttpServletRequest request) throws VerificationCodeException {
        String captcha = request.getParameter("captcha");
        String savedCode = (String) request.getSession().getAttribute("captcha");
        if (!StringUtils.isEmpty(savedCode)) {
            request.getSession().removeAttribute("captcha");
        }
        if (StringUtils.isEmpty(captcha) || StringUtils.isEmpty(savedCode) || !captcha.equals(savedCode)) {
            throw new VerificationCodeException();
        }
    }
}
