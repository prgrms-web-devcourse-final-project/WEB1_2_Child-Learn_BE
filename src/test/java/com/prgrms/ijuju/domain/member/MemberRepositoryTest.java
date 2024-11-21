package com.prgrms.ijuju.domain.member;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataJpa
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    //@Order(1)
    @Test
    @DisplayName("회원가입 테스트")
    public void joinTest() {

        // Given
        Member member = Member.builder()
                .loginId("test1234")
                .pw("test1234@")
                .username("test")
                .email("test@test.com")
                .birth(LocalDate.parse("1998-07-13"))
                .build();

        // When
        Member savedMember = memberRepository.save(member);

        // Then
        assertNotNull(savedMember);
        assertEquals("test1234", savedMember.getLoginId());
        assertEquals("test1234@", savedMember.getPw());
        assertEquals("test", savedMember.getUsername());
        assertEquals("test@test.com", savedMember.getEmail());
        assertEquals(LocalDate.parse("1998-07-13"), savedMember.getBirth());


    }

}
