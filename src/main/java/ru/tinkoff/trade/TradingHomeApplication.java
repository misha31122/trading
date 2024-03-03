package ru.tinkoff.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("ru.tinkoff.trade")
@EntityScan("ru.tinkoff.trade")
@SpringBootApplication
public class TradingHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingHomeApplication.class, args);
    }

}
