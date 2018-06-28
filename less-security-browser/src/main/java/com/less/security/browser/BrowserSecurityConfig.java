package com.less.security.browser;

import com.less.security.core.constants.SecurityConstants;
import com.less.security.core.properties.BrowserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

/**
 * @Author: Less
 * @Date: 2018/6/23 下午11:14
 **/
@EnableWebFluxSecurity
public class BrowserSecurityConfig {

    @Autowired
    private ServerAuthenticationSuccessHandler lessAuthenticationSuccessHandler;

    @Autowired
    private BrowserProperties browserProperties;

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("less")
                .password("123456")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .authenticationSuccessHandler(lessAuthenticationSuccessHandler)
                .and()
            .authorizeExchange()
                .pathMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        browserProperties.getLoginPage())
                .permitAll()
                .anyExchange()
                .authenticated();

        return http.build();
    }

}
