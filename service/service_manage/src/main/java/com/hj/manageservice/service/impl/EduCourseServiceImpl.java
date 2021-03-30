package com.hj.manageservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.manageservice.client.AclClient;
import com.hj.manageservice.entity.EduCourse;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.mapper.EduCourseMapper;
import com.hj.manageservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.manageservice.service.EduExperimentService;
import com.hj.manageservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private AclClient aclClient;
    @Autowired
    private EduExperimentService experimentService;
    @Autowired
    private FileService fileService;
    @Override
    public boolean AddCourse(EduCourse eduCourse) {
        String nameId = aclClient.getName();
        eduCourse.setUserCreate(nameId);
        int insert = baseMapper.insert(eduCourse);
        return insert>0;
    }
    public String getNameId(){
        return aclClient.getName();
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
}
