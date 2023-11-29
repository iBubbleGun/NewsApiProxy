package edu.hillel.newsapiproxy.configuration;

import com.kwabenaberko.newsapilib.NewsApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${api.news.key}")
    private String apiNewsKey;

    @Bean
    public NewsApiClient newsApiClient() {
        return new NewsApiClient(apiNewsKey);
    }
}
