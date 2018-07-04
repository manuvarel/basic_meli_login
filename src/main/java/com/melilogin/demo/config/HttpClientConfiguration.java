package com.melilogin.demo.config;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfiguration {

//  private static final Logger log = LoggerFactory.getLogger(HttpClientConfiguration.class);

  @Autowired
  private Environment environment;

  @Bean
  public ClientHttpRequestFactory httpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory(httpClient());
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
//    restTemplate.setInterceptors(ImmutableList.of((request, body, execution) -> {
//      byte[] token = Base64.encodeBase64((format("%s:%s", environment.getProperty("fake.username"), environment.getProperty("fake.password"))).getBytes());
//      request.getHeaders().add("Authorization", format("Basic %s", new String(token)));
//
//      return execution.execute(request, body);
//    }));

    return restTemplate;
  }

  @Bean
  public HttpClient httpClient() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    // Get the poolMaxTotal value from our application[-?].yml or default to 10 if not explicitly set
    connectionManager.setMaxTotal(environment.getProperty("poolMaxTotal", Integer.class, 10));

    return HttpClientBuilder
      .create()
      .setConnectionManager(connectionManager)
      .build();
  }

  /**
   * Just for demonstration
   */
  @PostConstruct
  public void debug() {
//    log.info("Pool max total: {}", environment.getProperty("poolMaxTotal", Integer.class));
  }
}