package com.less.security.browser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.less.security.core.constants.LoginType;
import com.less.security.core.constants.SecurityConstants;
import com.less.security.core.properties.BrowserProperties;
import com.less.security.core.suport.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;

/**
 * 浏览器安全控制器
 *
 * @Author: Less
 * @Date: 2018/6/24 下午4:03
 **/
@Slf4j
@RestController
public class BrowserSecurityController {

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    private ServerRequestCache requestCache = new WebSessionServerRequestCache();

    @Autowired
    private BrowserProperties browserProperties;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 当需要身份验证时跳转到这里
     *
     * @param exchange
     * @return
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<Void> requireAuthentication(ServerWebExchange exchange) throws IOException {

        log.info("需要登录认证：{}");

        return requestCache.getRedirectUri(exchange).flatMap(uri -> {
            String path = uri.getPath();
            log.info("需要登录认证：{}", path);

            if (browserProperties.getLoginType() == LoginType.REDIRECT) {
                // 登录类型为转发

                return redirectStrategy.sendRedirect(exchange, URI.create(browserProperties.getLoginPage()));
            } else {
                // 登录类型为json，返回json数据

                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                DataBufferFactory factory = response.bufferFactory();

                Mono<DataBuffer> body = null;
                try {
                    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.UNAUTHORIZED);
                    SimpleResponse simpleResponse = SimpleResponse.fail(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    String content = objectMapper.writeValueAsString(responseEntity);
                    body = Mono.just(factory.wrap(content.getBytes()));

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e.getMessage());
                }

                return response.writeWith(body);
            }

        });

    }

}
