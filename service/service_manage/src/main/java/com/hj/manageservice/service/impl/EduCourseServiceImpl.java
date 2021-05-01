package com.hj.manageservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.commonutils.vo.UserVo;
import com.hj.commonutils.vo.UserVos;
import com.hj.manageservice.client.AclClient;
import com.hj.manageservice.entity.EduCourse;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.mapper.EduCourseMapper;
import com.hj.manageservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.manageservice.service.EduExperimentService;
import com.hj.manageservice.service.FileService;
import com.hj.manageservice.entity.vo.TeacherCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduExperimentService experimentService;
    @Autowired
    private FileService fileService;
    @Autowired
    private AclClient aclClient;
    @Override
    public boolean AddCourse(EduCourse eduCourse) {
        eduCourse.setRealityNumber(0);
        int insert = baseMapper.insert(eduCourse);
        return insert>0;
    }

    @Override
    @Transactional
    public boolean removeCourse(String id) {
        //根据id删除课程
        int i = baseMapper.deleteById(id);
        //根据课程id删除文件
        QueryWrapper<EduExperiment>  exWrapper = new QueryWrapper<>();
        exWrapper.eq("course_id",id);
        List<EduExperiment> experiments = experimentService.list(exWrapper);
        boolean deleteByCourseId = experimentService.deleteByCourseId(id);
        //根据课程id删除实验
        boolean fileFlag= false;
        for (EduExperiment experiment :experiments) {
            fileFlag = fileService.deleteByExperimentId(experiment.getId());
        }
        return i>0&&deleteByCourseId&&fileFlag;
    }

    @Override
    public List<TeacherCourse> getTeacherCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        Calendar now = new GregorianCalendar();
        String yearBegin =(int)(now.get(Calendar.YEAR)-1)+"-01-01 00:00:00";

        wrapper.ge("gmt_create",yearBegin);
        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        UserVos userList1 = aclClient.getUserList();
        List<TeacherCourse> teacherCourseList = new ArrayList<>();
        List<UserVo> userVoList = userList1.getUserVoList();
        for (UserVo userVo: userVoList){
            TeacherCourse teacherCourse1 = new TeacherCourse();
            teacherCourse1.setValue(userVo.getId());
            teacherCourse1.setLabel(userVo.getName());
            List<TeacherCourse> teacherCourseList2 = new ArrayList<>();
            for (EduCourse eduCourse:eduCourses) {
                TeacherCourse teacherCourse2 = new TeacherCourse();
                if(userVo.getName().equals(eduCourse.getUserCreate())){
                    teacherCourse2.setValue(eduCourse.getId());
                    teacherCourse2.setLabel(eduCourse.getName());
                    teacherCourseList2.add(teacherCourse2);
                }
            }
            teacherCourse1.setChildren(teacherCourseList2);
            teacherCourseList.add(teacherCourse1);
        }
        return teacherCourseList;
    }
}
