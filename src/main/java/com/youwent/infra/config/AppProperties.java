package com.youwent.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 개발 환경에 따라 다른 host에 접근하기 위해 추상화시킴.
@Data
@Component
// prefix
@ConfigurationProperties("app")
public class AppProperties {
    private String host;
}
