package com.hj.manageservice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EduExperimentVo {
    @ApiModelProperty(value = "实验名称")
    private String name;

    @ApiModelProperty(value = "实验内容")
    private String description;

    @ApiModelProperty(value = "实验学习视频")
    private String videoId;
    private List<String> FileList;
}
