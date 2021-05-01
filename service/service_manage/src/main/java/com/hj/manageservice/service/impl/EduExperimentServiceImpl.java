package com.hj.manageservice.service.impl;

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
import com.hj.manageservice.entity.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(!eduExperiment.getFileList().isEmpty()){
            QueryWrapper<EduExperiment> wrapper= new QueryWrapper<>();
            wrapper.eq("name",eduExperiment.getName());
            wrapper.eq("description",eduExperiment.getDescription());
            wrapper.eq("course_id",eduExperiment.getCourseId());
            EduExperiment eduExperiment1 = baseMapper.selectOne(wrapper);
            List<File> fileList = new ArrayList<>();//此次file为自定义的file
            // 将文件表和实验表关联
            for (FileVo fileUrl: eduExperiment.getFileList()) {
                File fileU = new File();// 此file为自己的entity对象的file
                fileU.setUrl(fileUrl.getUrl());
                fileU.setExperimentId(eduExperiment1.getId());
                fileU.setFileName(fileUrl.getName());
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
        if(!StringUtils.isEmpty(experimentVO.getCourseId())){
            wrapper.eq("course_id",experimentVO.getCourseId());
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
// 修改文件列表
    @Override
    @Transactional
    public boolean updateExperiment(EduExperimentVo eduExperiment) {
        EduExperiment experiment = new EduExperiment();
        BeanUtils.copyProperties(eduExperiment,experiment);
        boolean update = experimentService.updateById(experiment);
        // 获取实验中所有的文件
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("experiment_id",eduExperiment.getId());
        List<File> files = fileService.list(wrapper);
        //如果该实验没有添加任何文件
        if(files.isEmpty()){
            // 直接全部上传
            if(!eduExperiment.getFileList().isEmpty()){
                List<File> fileList = new ArrayList<>();//此次file为自定义的file
                // 将文件表和实验表关联
                for (FileVo fileUrl: eduExperiment.getFileList()) {
                    File fileU = new File();// 此file为自己的entity对象的file
                    fileU.setUrl(fileUrl.getUrl());
                    fileU.setExperimentId(eduExperiment.getId());
                    fileU.setFileName(fileUrl.getName());
                    fileList.add(fileU);
                }
                boolean saveBatch = fileService.saveBatch(fileList);
                return saveBatch&&update;
            }else {
                return update;
            }
        }
        //保存要是删除文件的id
        List<String> deleteId = new ArrayList<>();
        //如果用户传来的文件列表为空，直接全删除该实验中的文件
        if(eduExperiment.getFileList().isEmpty()){
            files.forEach(e->{
                deleteId.add(e.getId());
            });
            boolean remove = fileService.removeByIds(deleteId);
            return remove&&update;
        }
        //保存要添加文件
        List<File> fileList = new ArrayList<>();//此次file为自定义的file
        Map<String,Integer> map = new HashMap<String,Integer>();
        // 得到未添加实验文件
        files.forEach(e->{
            map.put(e.getUrl(),1);//数字1为只有数据库中有，需要删除
        });
        eduExperiment.getFileList().forEach(e->{
            if (map.get(e.getUrl()) == 1) {
                map.put(e.getUrl(), 2);//数组2为没有进行任何操作
            } else {
                map.put(e.getUrl(), 3);//数字3为前端要上传的文件
            }
        });
        //遍历将删除id保存
        files.forEach(e->{
            if(map.get(e.getUrl())==1){
                deleteId.add(e.getId());
            }
        });
        //遍历保存添加的文件
        eduExperiment.getFileList().forEach(e->{
            if (map.get(e.getUrl())==3){
                File file = new File();
                file.setFileName(e.getName());
                file.setUrl(e.getUrl());
                file.setExperimentId(eduExperiment.getId());
                fileList.add(file);
            }
        });
        if(!fileList.isEmpty()){
            //批量添加
            fileService.saveBatch(fileList);
        }
        if(!deleteId.isEmpty()){
            //批量删除文件
            fileService.removeByIds(deleteId);
        }
        return update;
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
