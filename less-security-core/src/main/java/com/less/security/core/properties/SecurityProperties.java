package com.less.security.core.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 所有配置项
 *
 * @Author: Less
 * @Date: 2018/6/24 上午12:23
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "less.security")
public class SecurityProperties {

    @Autowired
    private BrowserProperties browser;

}
