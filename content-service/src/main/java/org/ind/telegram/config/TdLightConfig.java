package org.ind.telegram.config;

import it.tdlight.client.SimpleTelegramClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TdLightConfig {
    @Bean
    public SimpleTelegramClientFactory simpleTelegramClientFactory() {
        return new SimpleTelegramClientFactory();
    }
}
