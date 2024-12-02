package com.prgrms.ijuju.domain.ranking.service;

import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.ranking.dto.response.RankingResponse;
import com.prgrms.ijuju.domain.ranking.entity.Ranking;
import com.prgrms.ijuju.domain.ranking.repository.RankingRepository;
import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {

    private final MemberRepository memberRepository;
    private final RankingRepository rankingRepository;
    private final WalletRepository walletRepository;
    private final FriendListRepository friendListRepository;
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime weekStart = now.withHour(9).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.MONDAY);
    LocalDateTime weekEnd = weekStart.plusDays(7);

    public void updateRanking() {

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            Long weeklyPoints = walletRepository.findByMemberIdAndCreatedAtBetween(member.getId(), weekStart, weekEnd)
                    .map(Wallet::getCurrentPoints)
                    .orElse(0L);
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

    public void updateBulkWeekStart() {
        rankingRepository.updateWeekStartAndEnd(weekStart, weekEnd);
        rankingRepository.resetWeeklyPoints();
    }

    @Transactional(readOnly = true)
    public Page<RankingResponse> showAllRankingList(Pageable pageable) {
        Page<Ranking> allByOrderByWeeklyPointsDesc = rankingRepository.findAllByOrderByWeeklyPointsDesc(pageable);

        return allByOrderByWeeklyPointsDesc.map(ranking -> RankingResponse.of(findRankByMemberId(ranking.getMember().getId()), ranking.getMember().getUsername(), ranking.getWeeklyPoints()));
    }

    // 친구의 랭킹 조회
    @Transactional(readOnly = true)
    public Page<RankingResponse> showFriendRankingList(Long memberId, Pageable pageable) {
        List<Long> friendIds = friendListRepository.findAllByMemberId(memberId).stream()
                .map(friendList -> friendList.getFriend().getId())
                .collect(Collectors.toList());

        // 자신의 ID도 친구 목록에 추가
        if (!friendIds.contains(memberId)) {
            friendIds.add(memberId);
        }

        return rankingRepository.findAllByMemberIdInOrderByWeeklyPointsDesc(friendIds, pageable)
                .map(ranking -> RankingResponse.of(
                        findRankByMemberId(ranking.getMember().getId()),
                        ranking.getMember().getUsername(),
                        ranking.getWeeklyPoints()
                ));
    }

    @Transactional(readOnly = true)
    public Long findRankByMemberId(Long memberId) {
        return rankingRepository.findRankByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Long findWeeklyPointsByMemberId(Long memberId) {
        return rankingRepository.findByMemberId(memberId)
                .map(Ranking::getWeeklyPoints)
                .orElse(0L);
    }

}
