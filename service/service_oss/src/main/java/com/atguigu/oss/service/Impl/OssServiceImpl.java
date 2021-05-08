package com.atguigu.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            //获取上传的文件的名称
            String filename = file.getOriginalFilename();
            //为了防止上传的文件名相同，所以加上随机数，且通过日期进行分类
            String s = UUID.randomUUID().toString();
            filename = s + filename;
            String dateString = new DateTime().toString("yyyy/MM/dd");
            filename = dateString + "/" + filename;
            //第一个参数：bucket名称
            //第二个参数：路径/文件名称，例如：a/b/c.jpg
            //第三个参数：输入流
            ossClient.putObject(bucketName,filename,inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //获取上传到oss后文件的url
            String url = "https://" + bucketName + "." + endpoint + "/" + filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
