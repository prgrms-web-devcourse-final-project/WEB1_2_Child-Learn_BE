package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.stock.begin.entity.LimitBeginStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class LimitBeginStockRepositoryTest {
    @Autowired
    private LimitBeginStockRepository limitBeginStockRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 회원의 첫 플레이 시 기록을 생성한다.")
    void createNewPlayRecord() {
        // given
        Member member = Member.builder()
                .loginId("tester")
                .pw("testpw")
                .username("나는테스터")
                .email("test@example.com")
                .birth(LocalDate.now())
                .points(1000L)
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        LimitBeginStock limitBeginStock = limitBeginStockRepository.findByMemberId(savedMember.getId())
                .orElseGet(() -> {
                    LimitBeginStock newLimitDate =  new LimitBeginStock(savedMember);
                    return limitBeginStockRepository.save(newLimitDate);
                });

        // then
        assertEquals(limitBeginStock.getPlayer().getUsername(), savedMember.getUsername());
        assertEquals(limitBeginStock.getLastPlayedDate(), LocalDate.now());
    }

    @Test
    @DisplayName("기존 멤버 플레이 시 마지막 플레이 일자를 갱신한다.")
    void updateExisitingPlayRecord() {
        //given
        Member member = Member.builder()
                .loginId("tester2")
                .pw("testpw")
                .username("나는테스터2")
                .email("test@example.com")
                .birth(LocalDate.now().minusDays(1))
                .points(1000L)
                .build();
        Member savedMember = memberRepository.save(member);

        LimitBeginStock existingLimit = limitBeginStockRepository.save(new LimitBeginStock(savedMember));
        ReflectionTestUtils.setField(existingLimit, "lastPlayedDate", LocalDate.now().minusDays(1));
        limitBeginStockRepository.save(existingLimit);
        System.out.println(existingLimit.getLastPlayedDate());

        // when
        LimitBeginStock updatedLimit = limitBeginStockRepository.findByMemberId(savedMember.getId())
                .orElseGet(() -> {
                    LimitBeginStock newLimitDate =  new LimitBeginStock(savedMember);
                    return limitBeginStockRepository.save(newLimitDate);
                });

        // then
        assertEquals(updatedLimit.getLastPlayedDate(), LocalDate.now());
        System.out.println(updatedLimit.getLastPlayedDate());
    }
}
