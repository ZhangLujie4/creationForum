package com.zlj.forum.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.zlj.forum.common.config.OSSConstantConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhanglujie
 * @description
 * @date 2019-06-08 12:13
 */

@Slf4j
@Component
public class AliyunOSSUtil {

    @Autowired
    private OSSConstantConfig constantConfig;

    public String upload(String filename, InputStream in) {
        log.info("------OSS文件上传开始--------"+filename);
        String endpoint = constantConfig.getEndpoint();
        String accessKeyId = constantConfig.getKeyid();
        String accessKeySecret = constantConfig.getKeysecret();
        String bucketName = constantConfig.getBucket();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");;
        String dateStr = format.format(new Date());

        if (filename == null) {
            return null;
        }
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
            }

            // 设置文件路径和名称
            String fileUrl = dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + filename;
            // 上传文件
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength((long)in.available());
            PutObjectResult result = client.putObject(bucketName, fileUrl, in, meta);
            // 设置权限(公开读)
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                log.info("------OSS文件上传成功------{}", fileUrl);
                return "https://" + bucketName + "." + endpoint + "/" + fileUrl;
            }
        }catch (OSSException oe){
            log.error(oe.getMessage());
        }catch (ClientException ce){
            log.error(ce.getErrorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(client!=null){
                client.shutdown();
            }
        }
        return null;
    }
}
