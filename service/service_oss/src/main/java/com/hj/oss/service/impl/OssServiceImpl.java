package com.hj.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hj.oss.service.OssService;
import com.hj.oss.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 获取文件名称
        String filename ="teacher/"+UUID.randomUUID().toString().replaceAll("-", "")+"/"+file.getOriginalFilename();
        return getUrl(file,filename);
    }


    @Override
    public List<String> uploadImg(String[] imgLists,String id,String eid) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        List<String> imgUrls = new ArrayList<>();
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            for (String imgBase64:imgLists) {
                String imgBase = imgBase64.substring(imgBase64.indexOf(",")+1);

                BASE64Decoder decoder = new BASE64Decoder();
                byte[] bytes = decoder.decodeBuffer(imgBase);
                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) {// 调整异常数据
                        bytes[i] += 256;
                    }
                }
                InputStream inputStream = new ByteArrayInputStream(bytes);
                String filename = "student/"+id+"/"+eid+"/"+UUID.randomUUID().toString().replaceAll("-", "")+"."+imgBase64.substring(imgBase64.indexOf("/")+1,imgBase64.indexOf(";"));
                // 获取文件名称
                ossClient.putObject(bucketName,filename, inputStream);
                // 创建存储空间。
                ossClient.createBucket(bucketName);
                //把上传的文件路径返回
                String url = "https://"+bucketName+"."+endpoint+"/"+filename;
                imgUrls.add(url);
            }
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgUrls;
    }

    @Override
    public String uploadExcel(MultipartFile file) {
        String filename ="/xpcAttr/"+UUID.randomUUID().toString().replaceAll("-", "")+"/"+file.getOriginalFilename();
        return getUrl(file,filename);
    }
    //上传
    private String getUrl(MultipartFile file,String filename){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = file.getInputStream();

            ossClient.putObject(bucketName,filename, inputStream);

            // 创建存储空间。
            ossClient.createBucket(bucketName);
            // 关闭OSSClient。
            ossClient.shutdown();
            //把上传的文件路径返回
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
