package com.prgrms.ijuju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class IjujuApplication {

    public static void main(String[] args) {
        SpringApplication.run(IjujuApplication.class, args);
    }

}
