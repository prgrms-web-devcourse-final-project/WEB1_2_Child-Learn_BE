package com.prgrms.ijuju.domain.ranking.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.repository.PointDetailsRepository;
import com.prgrms.ijuju.domain.ranking.entity.Ranking;
import com.prgrms.ijuju.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {

    private final MemberRepository memberRepository;
    private final RankingRepository rankingRepository;
    private final PointDetailsRepository pointDetailsRepository;
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime weekStart = now.withHour(9).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.MONDAY);
    LocalDateTime weekEnd = weekStart.plusDays(7);

    public void updateRanking() {

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            Long weeklyPoints = pointDetailsRepository.calculateEarnedPointsForMember(member.getId(), weekStart, weekEnd);
            Optional<Ranking> existingRanking = rankingRepository.findByMemberId(member.getId());

            if (existingRanking.isPresent()) {
                Ranking ranking = existingRanking.get();
                ranking.changeWeeklyPoints(weeklyPoints != null ? weeklyPoints : 0L); // dirty checking
            } else {
                Ranking newRanking = Ranking.builder()
                        .member(member)
                        .weekStart(weekStart)
                        .weekEnd(weekEnd)
                        .weeklyPoints(weeklyPoints != null ? weeklyPoints : 0L)
                        .build();
                rankingRepository.save(newRanking);
            }
        }

    }

    public void updateWeekStartAndEnd() {
        rankingRepository.updateWeekStartAndEnd(weekStart, weekEnd);
    }

    public Page<Ranking> getRankingList(Pageable pageable) {
        return rankingRepository.findAllByOrderByWeeklyPointsDesc(pageable);
    }

}
