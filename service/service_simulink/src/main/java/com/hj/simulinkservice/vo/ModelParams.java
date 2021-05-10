package com.hj.simulinkservice.vo;

import lombok.Data;

@Data
public class ModelParams {
    String blockName;
    String paramName;
    double [] val;
}
