package com.hj.manageservice.service;

import com.hj.manageservice.entity.EduExperiment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.manageservice.entity.vo.EduExperimentVo;
import com.hj.manageservice.entity.vo.ExperimentPages;
import com.hj.manageservice.entity.vo.ExperimentVO;

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

    boolean AddExperiment(EduExperimentVo eduExperiment);

    boolean deleteByCourseId(String courseId);

    boolean deleteById(String experimentId);

    List<EduExperiment> GetListEXperiment(String courseId);
    ExperimentPages getPages(Long page, Long limit, ExperimentVO experimentVO);

    boolean updateExperiment(EduExperimentVo eduExperiment);
}
