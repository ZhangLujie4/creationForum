package com.zlj.forum.common.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-19 22:28
 */

@Configuration
public class ElasticSearchConfig {


    /**
     * elastic:
     *     cluster-name: zlj_cluster_dev
     *     cluster-node-ip: 47.95.215.194
     *     cluster-node-host: 9300
     * @return
     * @throws UnknownHostException
     */

    @Bean
    public TransportClient client() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
            InetAddress.getByName("47.95.215.194"),
            9300
        );

        Settings settings = Settings.builder()
                .put("cluster.name", "zlj_cluster_dev")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);

        return client;
    }
}
