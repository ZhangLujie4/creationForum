package com.zlj.forum.web.controller;

import com.zlj.forum.common.utils.AliyunOSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhanglujie
 * @description
 * @date 2019-06-08 12:41
 */

@Slf4j
@RestController
@RequestMapping("/api/common")
public class UploadController {

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @PostMapping("/upload")
    public String uploadBlog(@RequestParam("file") MultipartFile file) {
        log.info("文件上传开始");
        String filename = file.getOriginalFilename();
        String url = "";
        try {
            if (file!=null) {
                if (!"".equals(filename.trim())) {
                    InputStream inputStream = new ByteArrayInputStream(file.getBytes());
                    // 上传到OSS
                    url = aliyunOSSUtil.upload(filename, inputStream);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.info("inputStream关闭失败");
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return url == null ? "" : url;
    }
}
