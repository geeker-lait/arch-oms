package org.arch.oms.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-06
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private ApplicationContext context;

    /**
     * redisson客户端
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        String activeProfile = context.getEnvironment().getActiveProfiles()[0];
        Config config = Config.fromYAML(new ClassPathResource("redisson-" + activeProfile + ".yml").getInputStream());
        return Redisson.create(config);
    }
}
