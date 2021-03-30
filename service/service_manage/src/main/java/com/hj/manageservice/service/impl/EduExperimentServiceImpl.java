package com.hj.manageservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.manageservice.entity.EduExperiment;
import com.hj.manageservice.entity.ExperimentCourse;
import com.hj.manageservice.entity.File;
import com.hj.manageservice.mapper.EduExperimentMapper;
import com.hj.manageservice.service.EduExperimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.manageservice.service.ExperimentCourseService;
import com.hj.manageservice.service.FileService;
import com.hj.manageservice.vo.EduExperimentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
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
    private ExperimentCourseService experimentCourseService;
    @Autowired
    private FileService fileService;
    @Override
    @Transactional
    public boolean AddExperiment(EduExperimentVo eduExperiment, String courseId) {
        EduExperiment experiment = new EduExperiment();
        BeanUtils.copyProperties(experiment,eduExperiment);
        // 向实验表中插入数据
        int insert = baseMapper.insert(experiment);
        // 将实验表和课程表关联
        QueryWrapper<EduExperiment> wrapper= new QueryWrapper<>();
        wrapper.eq("name",eduExperiment.getDescription());
        EduExperiment eduExperiment1 = baseMapper.selectOne(wrapper);
        ExperimentCourse experimentCourse = new ExperimentCourse();
        experimentCourse.setCourseId(courseId);
        experimentCourse.setExperimentId(eduExperiment1.getId());
        boolean save = experimentCourseService.save(experimentCourse);

        List<File> fileList = new ArrayList<>();
        File fileU = new File();// 此file为自己的entity对象的file
        fileU.setExperimentId(eduExperiment1.getId());
        // 将文件表和实验表关联
        for (String fileUrl: eduExperiment.getFileList()) {
            fileU.setUrl(fileUrl);
            fileList.add(fileU);
        }
        boolean saveBatch = fileService.saveBatch(fileList);
        return insert>0&&save&&saveBatch;
    }

    @Override
    public List<EduExperiment> GetListEXperiment(String courseId) {
        QueryWrapper<ExperimentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<ExperimentCourse> experimentCourses = experimentCourseService.list(wrapper);
        List<String> eduExperimentId= new ArrayList<>();
        for (ExperimentCourse experimentCourse: experimentCourses) {
            eduExperimentId.add(experimentCourse.getId());
        }
        List<EduExperiment> eduExperiments = baseMapper.selectBatchIds(eduExperimentId);
        return eduExperiments;
    }

    /**
     * 根据课程id删除实验
     * @param courseId
     * @return
     */
    @Override
    public boolean deleteByCourseId(String courseId) {
        QueryWrapper<ExperimentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        boolean remove = experimentCourseService.remove(wrapper);
        return remove;
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
