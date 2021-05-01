package com.hj.manageservice.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Scores {
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    private String userName;

    @ApiModelProperty(value = "实验id")
    private String experimentId;

    private String experimentName;

    @ApiModelProperty(value = "实验得分")
    private Integer score;

    @ApiModelProperty(value = "完成情况 1完成，0未完成")
    private Boolean finished;

    @ApiModelProperty(value = "是否评分情况 1是，0不是")
    private Boolean isScore;
}
