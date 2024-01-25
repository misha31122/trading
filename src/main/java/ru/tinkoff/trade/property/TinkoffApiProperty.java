package ru.tinkoff.trade.property;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "tinkoff")
public class TinkoffApiProperty {

  private RestProperties rest;

  @Data
  public static class RestProperties {
    private String basePath;
    private String token;
    private int maxTotalConn;
    private int defaultMaxConnPerRoute;
    private int socketTimeoutMillis;
    private int connRequestTimeoutMillis;
    private int connTimeoutMillis;
  }

}
