package com.hj.manageservice.service;

import com.hj.manageservice.entity.Img;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-04-24
 */
public interface ImgService extends IService<Img> {

    Boolean uploadImg(String[] imgLists, String userId,String ExId);
}
