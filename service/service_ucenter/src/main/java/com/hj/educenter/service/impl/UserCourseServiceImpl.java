package com.hj.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hj.commonutils.vo.CourseVo;
import com.hj.educenter.client.CourseClient;
import com.hj.educenter.entity.UserCourse;
import com.hj.educenter.mapper.UserCourseMapper;
import com.hj.educenter.service.UserCourseService;
import com.hj.educenter.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements UserCourseService {
    @Autowired
    private CourseClient courseClient;
    @Transactional
    @Override
    public ResUtil JoinCourse(UserCourse userCourse) {
        ResUtil resUtil = new ResUtil();
        //判断是否已近加入了班课
        QueryWrapper<UserCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("classid",userCourse.getClassid());
        wrapper.eq("userid",userCourse.getUserid());
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer >= 1){
            resUtil.setFlag(false);
            resUtil.setMessage("已加入班课！");
            return resUtil;
        }
        CourseVo course = courseClient.getCourse(userCourse.getClassid());
        // 判断当前人数是否超过容量
        if(course.getNumber()<=course.getRealityNumber()){
            resUtil.setFlag(false);
            resUtil.setMessage("已超过容量！");
            return resUtil;
        }
        //修改实际人数
        course.setRealityNumber(course.getRealityNumber()+1);
        courseClient.changeCourseNumber(course);
        // 添加当前用户和课程表的关联
        baseMapper.insert(userCourse);
        resUtil.setFlag(true);
        resUtil.setMessage("加入成功!");
        return resUtil;
    }

    @Transactional
    @Override
    public Boolean UnjoinCourse(String courseId,String userId) {
        QueryWrapper<UserCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("classid",courseId);
        wrapper.eq("userid",userId);
        int i = baseMapper.delete(wrapper);
        CourseVo course = courseClient.getCourse(courseId);
        //修改实际人数
        course.setRealityNumber(course.getRealityNumber()-i);
        Boolean number = courseClient.changeCourseNumber(course);
        return number&&i>0;
    }
}
