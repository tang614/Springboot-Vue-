package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired OssService ossService;
    //上传头像的方法
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //file是前端传来的文件,url是存储到阿里云OSS的url地址
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
