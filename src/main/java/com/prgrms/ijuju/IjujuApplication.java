package com.prgrms.ijuju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IjujuApplication {

    public static void main(String[] args) {
        SpringApplication.run(IjujuApplication.class, args);
    }

}
