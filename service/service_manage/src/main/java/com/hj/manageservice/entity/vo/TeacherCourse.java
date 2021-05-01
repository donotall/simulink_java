package com.hj.manageservice.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeacherCourse {
    String  value;// id
    String  label;// name
    List<TeacherCourse> children;
}
