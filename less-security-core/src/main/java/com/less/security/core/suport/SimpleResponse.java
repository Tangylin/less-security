package com.less.security.core.suport;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Rest API 响应实体
 *
 * @Author: Less
 * @Date: 2018/6/24 下午3:40
 **/
@Data
public class SimpleResponse {

    /**
     * 状态码
     */
    private int Status;

    /**
     * 响应结果
     */
    private Object result;

    /**
     * 成功响应
     *
     * @return
     */
    public static SimpleResponse success() {
        SimpleResponse response = new SimpleResponse();
        response.Status = HttpStatus.OK.value();
        response.result = "ok";
        return response;
    }

    /**
     * 失败响应
     *
     * @param status 状态码
     * @param result 响应信息
     * @return
     */
    public static SimpleResponse fail(int status, Object result) {
        SimpleResponse response = new SimpleResponse();
        response.Status = status;
        response.result = result;
        return response;
    }
}
