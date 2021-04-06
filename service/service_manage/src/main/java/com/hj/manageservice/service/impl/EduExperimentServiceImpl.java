package com.hj.manageservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.manageservice.entity.EduCourse;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.entity.File;
import com.hj.manageservice.mapper.EduExperimentMapper;
import com.hj.manageservice.service.EduCourseService;
import com.hj.manageservice.service.EduExperimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.manageservice.service.FileService;
import com.hj.manageservice.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
public class EduExperimentServiceImpl extends ServiceImpl<EduExperimentMapper, EduExperiment> implements EduExperimentService {

    @Autowired
    private FileService fileService;
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduExperimentService experimentService;
    @Override
    @Transactional
    public boolean AddExperiment(EduExperimentVo eduExperiment) {
        EduExperiment experiment = new EduExperiment();
        BeanUtils.copyProperties(eduExperiment,experiment);
        // 向实验表中插入数据
        int insert = baseMapper.insert(experiment);
        boolean flag = insert>0;
        if(eduExperiment.getFileList()!=null){
            QueryWrapper<EduExperiment> wrapper= new QueryWrapper<>();
            wrapper.eq("name",eduExperiment.getName());
            wrapper.eq("description",eduExperiment.getDescription());
            EduExperiment eduExperiment1 = baseMapper.selectOne(wrapper);
            List<File> fileList = new ArrayList<>();
            File fileU = new File();// 此file为自己的entity对象的file
            List<FileVo> fileVos = JSONObject.parseArray(eduExperiment.getFileList(), FileVo.class);
            fileU.setExperimentId(eduExperiment1.getId());
            // 将文件表和实验表关联
            for (FileVo fileUrl: fileVos) {
                fileU.setUrl(fileUrl.getFileUrl());
                fileU.setFileName(fileUrl.getFileName());
                fileList.add(fileU);
            }
            boolean saveBatch = fileService.saveBatch(fileList);
            flag = flag&& saveBatch;
        }

        return flag;
    }

    @Override
    public List<EduExperiment> GetListEXperiment(String courseId) {
        QueryWrapper<EduExperiment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduExperiment> eduExperiments = baseMapper.selectList(wrapper);
        return eduExperiments;
    }

    @Override
    public ExperimentPages getPages(Long page, Long limit, ExperimentVO experimentVO) {
        Page<EduExperiment> pageParam = new Page<>(page, limit);
        QueryWrapper<EduExperiment> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(experimentVO.getName())) {
            wrapper.like("name",experimentVO.getName());
        }
        if(!StringUtils.isEmpty(experimentVO.getBegin())){
            wrapper.ge("gmt_create",experimentVO.getBegin());
        }
        if(!StringUtils.isEmpty(experimentVO.getEnd())){
            wrapper.le("gmt_end",experimentVO.getEnd());
        }
        experimentService.page(pageParam, wrapper);
        List<ExperimentPage> experimentPageList = new ArrayList<>();
        ExperimentPages experimentPages = new ExperimentPages();

        for (EduExperiment edu:pageParam.getRecords()) {
            EduCourse byId = eduCourseService.getById(edu.getCourseId());
            ExperimentPage experimentPage = new ExperimentPage();
            experimentPage.setCourseName(byId.getName());
            BeanUtils.copyProperties(edu,experimentPage);
            experimentPage.setUserCreate(byId.getUserCreate());
            experimentPageList.add(experimentPage);
        }
        experimentPages.setExperimentPageList(experimentPageList);
        experimentPages.setTotal(pageParam.getTotal());
        return experimentPages;
    }

    /**
     * 根据id删除
     * @param courseId
     * @return
     */
    @Override
    public boolean deleteByCourseId(String courseId) {
        QueryWrapper<EduExperiment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        int remove = baseMapper.delete(wrapper);
        return remove>0;
    }

    /**
     * 根据id删除实验和实验文件
     * @param experimentId
     * @return
     */
    @Override
    @Transactional
    public boolean deleteById(String experimentId) {
        int deleteById = baseMapper.deleteById(experimentId);
        boolean deleteByExperimentId = fileService.deleteByExperimentId(experimentId);
        return deleteByExperimentId&&deleteById>0;
    }
}
