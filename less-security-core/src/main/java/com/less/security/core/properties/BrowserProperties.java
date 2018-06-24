package com.less.security.core.properties;

import com.less.security.core.constants.LoginType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 浏览器配置项
 *
 * @Author: Less
 * @Date: 2018/6/24 上午12:26
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "less.security.browser")
public class BrowserProperties {

    /**
     * 登录页
     */
    private String loginPage = "/login.html";

    /**
     * 登录类型
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 退出页
     */
    private String logoutPage = loginPage + "?logout";

    /**
     * 注册页
     */
    private String signUpUrl = "/signUp.html";

    /**
     * 记住我保持时间（秒)。默认7天
     */
    private int rememberMeSeconds = 3600 * 7;

}
