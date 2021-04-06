package com.hj.manageservice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EduExperimentVo {
    @ApiModelProperty(value = "实验名称")
    private String name;

    @ApiModelProperty(value = "实验内容")
    private String description;

    @ApiModelProperty(value = "实验学习视频")
    private String videoId;
    @ApiModelProperty(value = "结束时间")
    private Date gmtEnd;
    @ApiModelProperty(value = "课程id")
    private  String courseId;
    @ApiModelProperty(value = "文件列表")
    private String fileList;
}
