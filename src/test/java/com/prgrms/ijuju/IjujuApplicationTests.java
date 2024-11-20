package com.prgrms.ijuju;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnableJpaAuditing
class IjujuApplicationTests {

    @Test
    void contextLoads() {
    }

}
