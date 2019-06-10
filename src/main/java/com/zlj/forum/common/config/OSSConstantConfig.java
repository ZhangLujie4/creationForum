package com.zlj.forum.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhanglujie
 * @description
 * @date 2019-06-08 11:35
 */

@Data
@Component
@Configuration
public class OSSConstantConfig {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.keyid}")
    private String keyid;

    @Value("${oss.keysecret}")
    private String keysecret;

    @Value("${oss.bucket}")
    private String bucket;
}
