package com.codecool.beerlovers.beerdb.config;


import com.codecool.beerlovers.beerdb.util.HttpRequestToJsonString;
import com.codecool.beerlovers.beerdb.util.HttpRequestToJsonStringImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@ComponentScan(basePackages = "com.codecool.beerlovers.beerdb")
public class AppConfig {

    @Bean
    public EntityManager entityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("beersJPA");
        return emf.createEntityManager();
    }

    @Bean
    HttpRequestToJsonString requestToJsonString() {
        return new HttpRequestToJsonStringImpl();
    }

}