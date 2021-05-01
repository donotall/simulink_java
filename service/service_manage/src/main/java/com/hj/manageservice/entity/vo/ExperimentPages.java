package com.hj.manageservice.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ExperimentPages {
    List<ExperimentPage> experimentPageList;
    long total;
}
