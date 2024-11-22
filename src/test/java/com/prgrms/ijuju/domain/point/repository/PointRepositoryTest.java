package com.prgrms.ijuju.domain.point.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.entity.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PointRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setUp() { // 테스트 데이터 초기화
        member = Member.builder()
                .loginId("test1234")
                .pw("test1234@")
                .username("test")
                .email("test@test.com")
                .birth(LocalDate.parse("1998-07-13"))
                .build();
        memberRepository.save(member);
    }

    @Test
    @Rollback(false)
    public void testFindByMemberId() { // 포인트가 있을 때 조회 테스트
        Point point = new Point();
        point.setMember(member);
        point.setCurrentPoints(100L);
        pointRepository.save(point);

        Optional<Point> foundPoint = pointRepository.findByMemberId(member.getId());
        assertThat(foundPoint).isPresent();
        assertThat(foundPoint.get().getCurrentPoints()).isEqualTo(100L);
    }

    @Test
    @Rollback(false)
    public void testUpdateCurrentPoints() { // 포인트 업데이트 테스트
        Member testMember = Member.builder()
                .loginId("test5678")
                .pw("test5678@")
                .username("tester")
                .email("tester@test.com")
                .birth(LocalDate.parse("1998-07-13"))
                .build();
        memberRepository.save(testMember);
        
        Point point = new Point();
        point.setMember(testMember);
        point.setCurrentPoints(100L);
        pointRepository.save(point);

        point.setCurrentPoints(200L);
        pointRepository.flush();

        Optional<Point> updatedPoint = pointRepository.findByMemberId(testMember.getId());
        assertThat(updatedPoint).isPresent();
        assertThat(updatedPoint.get().getCurrentPoints()).isEqualTo(200L);
    }
} 
