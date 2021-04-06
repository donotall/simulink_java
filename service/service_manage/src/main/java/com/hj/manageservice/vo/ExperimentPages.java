package com.hj.manageservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class ExperimentPages {
    List<ExperimentPage> experimentPageList;
    long total;
}
