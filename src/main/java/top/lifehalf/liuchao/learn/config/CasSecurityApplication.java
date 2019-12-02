/*
 * Copyright 2019 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package top.lifehalf.liuchao.learn.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Arrays;

/**
 * <p>
 *
 * </p>
 *
 * @author liuchao4
 * @since 2019/11/29 18:43
 */
@Configuration
public class CasSecurityApplication {

    @Value("${cas.server.prefix}")
    private String casServerPrefix;

    @Value("${cas.server.login}")
    private String casServerLogin;

    @Value("${cas.server.logout}")
    private String casServerLogout;

    @Value("${cas.client.login}")
    private String casClientLogin;

    @Value("${cas.client.logout}")
    private String casClientLogout;

    @Value("${cas.client.logout.relative}")
    private String casClientLogoutRelative;

    @Value("${cas.user.inmemory}")
    private String casUserInMemory;

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casClientLogin);
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    @Bean
    @Primary
    public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties sp) {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(casServerLogin);
        entryPoint.setServiceProperties(sp);
        return entryPoint;
    }

    @Bean
    public TicketValidator ticketValidator() {
        return new Cas20ProxyTicketValidator(casServerPrefix);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(casUserInMemory).password("").roles("USER").build());
        return manager;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(ServiceProperties sp, TicketValidator ticketValidator,
                                                               UserDetailsService userDetailsService) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(sp);
        provider.setTicketValidator(ticketValidator);
        provider.setUserDetailsService(userDetailsService);
        provider.setKey("liuchao");
        return provider;
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties sp,
                                                           AuthenticationProvider authenticationProvider) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(sp);
        filter.setAuthenticationManager(new ProviderManager(Arrays.asList(authenticationProvider)));
        return filter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casServerPrefix);
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casServerLogout, new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casClientLogoutRelative);
        return logoutFilter;
    }

}
