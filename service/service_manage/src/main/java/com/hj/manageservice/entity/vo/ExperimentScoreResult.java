package com.hj.manageservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExperimentScoreResult {
    private String id;

    @ApiModelProperty(value = "实验名称")
    private String name;

    @ApiModelProperty(value = "实验内容")
    private String description;

    @ApiModelProperty(value = "课程id")
    private String courseId;
    private ExData exData;
    private String courseName;
    private String userCreate;
}
