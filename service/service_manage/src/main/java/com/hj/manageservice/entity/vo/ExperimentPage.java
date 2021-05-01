package com.hj.manageservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExperimentPage {

    @ApiModelProperty(value = "实验id")
    String id;

    @ApiModelProperty(value = "实验名称")
    private String name;

    @ApiModelProperty(value = "班课名称")
    String courseName;

    @ApiModelProperty(value = "老师名字")
    String userCreate;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "结束时间")
    private Date gmtEnd;
}
