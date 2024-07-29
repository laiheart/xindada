package com.xin.xindada.ai;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsx
 * @date 2024-07-01
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {

    private String apiKey;

    @Bean
    public ClientV4 clientV4() {
        return new ClientV4.Builder(apiKey).build();
    }
}
