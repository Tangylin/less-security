package com.less.security.browser.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.less.security.core.constants.LoginType;
import com.less.security.core.properties.BrowserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 认证成功处理器
 *
 * @Author: Less
 * @Date: 2018/6/23 下午10:20
 **/
@Component
public class LessAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrowserProperties browserProperties;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        if (browserProperties.getLoginType() == LoginType.JSON) {
            // 返回json数据
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBufferFactory factory = response.bufferFactory();

            Mono<DataBuffer> body = null;
            try {
                body = Mono.just(factory.wrap(objectMapper.writeValueAsBytes(authentication)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }

            return response.writeWith(body);

        } else {
            // 转发
            return super.onAuthenticationSuccess(webFilterExchange, authentication);
        }
    }
}
