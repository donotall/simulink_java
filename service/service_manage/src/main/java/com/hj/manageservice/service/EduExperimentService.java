package com.hj.manageservice.service;

import com.hj.manageservice.entity.EduExperiment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.manageservice.vo.EduExperimentVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-03-30
 */
public interface EduExperimentService extends IService<EduExperiment> {

    boolean AddExperiment(EduExperimentVo eduExperiment, String courseId);

    List<EduExperiment> GetListEXperiment(String courseId);

    boolean deleteByCourseId(String courseId);

    boolean deleteById(String experimentId);
}
