package com.hj.oss.controller;

import com.hj.oss.service.OssService;
import com.hj.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description="阿里云文件管理")
@RestController
@RequestMapping("eduoss/fileoss/")
public class OssController {
    @Autowired
    private OssService ossService;
    //上传文件的方法
    @PostMapping
    public R uploadOssFile(@RequestParam("file") MultipartFile file){
        // 返回oss文件路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().message("文件上传成功").data("url", url);
    }
    //上传文件的方法
    @PostMapping
    public String uploadOssFiles(@RequestParam("file") MultipartFile file){
        // 返回oss文件路径
        String url = ossService.uploadFileAvatar(file);
        return url;
    }
}