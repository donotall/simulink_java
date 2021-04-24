package com.hj.educenter.service;

import com.hj.educenter.entity.UserCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.educenter.utils.ResUtil;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
public interface UserCourseService extends IService<UserCourse> {

    ResUtil JoinCourse(UserCourse userCourse);

    Boolean UnjoinCourse(String id,String courseId);
}
