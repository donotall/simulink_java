package com.hj.manageservice.service;

import com.hj.manageservice.entity.XpcAttr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hj
 * @since 2021-04-18
 */
public interface XpcAttrService extends IService<XpcAttr> {
   void saveBatchUseFile(MultipartFile file, XpcAttrService xpcAttrService);
}
