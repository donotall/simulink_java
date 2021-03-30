package com.hj.simulinkservice.vo;

import lombok.Data;

@Data
public class TargetSettingVo {
    //步长
    private double ts;
    // 最后停止时间
    private double tFinal;
    //连接时间
    private int timeOut;
}
