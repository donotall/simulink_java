package com.hj.oss.controller;

import com.hj.oss.service.OssService;
import com.hj.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description="阿里云文件管理")
@RestController
@RequestMapping("eduoss/fileoss/")
public class OssController {
    @Autowired
    private OssService ossService;
    //上传文件的方法
    @PostMapping("/file")
    public R uploadOssFile(@RequestParam("file") MultipartFile file){
        // 返回oss文件路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().message("文件上传成功").data("url", url).data("name",file.getOriginalFilename());
    }
    //上传文件的方法
    @PostMapping("/excel")
    public R uploadOssExcel(@RequestParam("file") MultipartFile file){
        // 返回oss文件路径
        String url = ossService.uploadExcel(file);
        return R.ok().message("文件上传成功").data("url", url).data("name",file.getOriginalFilename());
    }
    @PostMapping("img/{id}/{eid}")
    public List<String> uploadImg(@RequestBody String[] imgLists,@PathVariable String id,@PathVariable String eid){
        List<String> imgListUrl  = ossService.uploadImg(imgLists,id,eid);
        return imgListUrl;
    }
}
