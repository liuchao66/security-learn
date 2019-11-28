/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/25 17:37
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource);

        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                .antMatchers("/app/api/**", "/captcha.jpg", "/session/invalid").permitAll()
                .anyRequest().authenticated()
                .and()
//                .formLogin().loginPage("/myLogin.html").permitAll()
                // 自定义URL
//                .formLogin().loginPage("/myLogin.html").loginProcessingUrl("/login").permitAll()
                // 登录返回json，不跳转URL
                .formLogin()
//                .formLogin().authenticationDetailsSource(authenticationDetailsSource)
//                .loginPage("/myLogin.html").loginProcessingUrl("/auth/form")
//                .successHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) -> {
//                        httpServletResponse.setContentType("application/json;charset=UTF-8");
//                        PrintWriter out = httpServletResponse.getWriter();
//                        out.write("{\"error_code\":\"0\", \"message\":\"欢迎登录系统\"}");
//                })
//                .failureHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) -> {
//                        httpServletResponse.setContentType("application/json;charset=UTF-8");
//                        httpServletResponse.setStatus(401);
//                        PrintWriter out = httpServletResponse.getWriter();
//                        out.write("{\"error_code\":\"401\", \"name\":\"" + e.getClass() + "\", \"message\":\"" + e.getMessage() +  "\"}");
//                }).permitAll()
                .and()
                .csrf().disable()
//                .addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class)
//                .rememberMe().userDetailsService(userDetailsService)
////                .key("liuchao")
//                .tokenRepository(tokenRepository).tokenValiditySeconds(30)
//                .and()
//                .logout()
                .logout().logoutUrl("/myLogout")
//                .logoutSuccessUrl("/")
                .logoutSuccessHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) -> {
                    httpServletResponse.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = httpServletResponse.getWriter();
                    out.write("{\"error_code\":\"0\", \"message\":\"欢迎再次登录\"}");
                })
                .invalidateHttpSession(true)
//                .deleteCookies("").addLogoutHandler()
                .and()
//                .sessionManagement().sessionFixation().none()
//                .sessionManagement()
//                .invalidSessionUrl("/session/invalid")
//                .invalidSessionStrategy(new MyInvalidSessionStrategy())
                .sessionManagement().maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
                .sessionRegistry(redisSessionRegistry)

        ;
    }

    @Autowired
    private SpringSessionBackedSessionRegistry redisSessionRegistry;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private MyAuthenticationProvider myAuthenticationProvider;

//    @Autowired
//    private MyDaoAuthenticationProvider myDaoAuthenticationProvider;
//
//    @Autowired
//    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        JdbcUserDetailsManager manager = auth
////                .inMemoryAuthentication()
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(new MyPasswordEncoder()).getUserDetailsService();
//        if (!manager.userExists("user")) {
//            manager.createUser(User.withUsername("user").password("123").roles("USER").build());
//        }
//        if (!manager.userExists("admin")) {
//            manager.createUser(User.withUsername("admin").password("123").roles("USER", "ADMIN").build());
//        }

//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        auth.authenticationProvider(authenticationProvider);

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

//        auth.authenticationProvider(myAuthenticationProvider);

//        auth.authenticationProvider(myDaoAuthenticationProvider);
    }

    @Bean
    public Producer captcha() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}