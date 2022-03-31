package com.volta.datax.core.springBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.volta.datax.*"})
@EntityScan("com.volta.datax.*")
@EnableJpaRepositories(basePackages = {"com.volta.datax.*"})
public class DataXSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.volta.datax.core.springBoot.DataXSpringBootApplication.class, args);
    }
}
