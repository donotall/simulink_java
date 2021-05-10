package com.hj.manageservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class XpcAttrData {
    //存放IP地址
    @ExcelProperty(index = 0)
    private String ip;
    // 存放端口地址
    @ExcelProperty(index = 1)
    private Integer port;
}
