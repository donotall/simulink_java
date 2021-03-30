package com.hj.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 生成有参数的构造方法
@NoArgsConstructor //无参构造
public class SLException extends RuntimeException {
    @ApiModelProperty(value = "状态码")
    private Integer code; //状态码

    private String msg; //提示信息
}
