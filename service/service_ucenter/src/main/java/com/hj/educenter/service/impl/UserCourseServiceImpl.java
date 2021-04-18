package com.hj.educenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hj.educenter.entity.UserCourse;
import com.hj.educenter.mapper.UserCourseMapper;
import com.hj.educenter.service.UserCourseService;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements UserCourseService {
}
