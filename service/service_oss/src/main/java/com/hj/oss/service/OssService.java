package com.hj.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public  interface OssService {
    // 上传文件到oss
    String uploadFileAvatar(MultipartFile file);

    List<String> uploadImg(String[] imgLists,String id,String eid);

    String uploadExcel(MultipartFile file);
}
