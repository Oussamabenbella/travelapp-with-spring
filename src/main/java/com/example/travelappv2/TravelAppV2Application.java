package com.example.travelappv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    H2ConsoleAutoConfiguration.class
})
public class TravelAppV2Application {

    public static void main(String[] args) {
        SpringApplication.run(TravelAppV2Application.class, args);
    }
}
