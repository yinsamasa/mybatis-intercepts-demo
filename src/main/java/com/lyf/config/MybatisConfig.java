package com.lyf.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MybatisConfig {



    @Bean
    public FenbiaoPlugin configurationCustomizer() {
        return  new FenbiaoPlugin();
    }

}
