package com.hj.manageservice.service;

import com.hj.manageservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
public interface EduCourseService extends IService<EduCourse> {

    boolean AddCourse(EduCourse eduCourse);
    String getNameId();
    boolean removeCourse(String id);
}
