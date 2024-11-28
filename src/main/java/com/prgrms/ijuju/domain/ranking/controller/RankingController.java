package com.prgrms.ijuju.domain.ranking.controller;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberTaskException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.ranking.dto.response.RankingResponse;
import com.prgrms.ijuju.domain.ranking.service.RankingService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rank")
public class RankingController {
    private final RankingService rankingService;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<RankingResponse> findMemberRankByUsername(
            @RequestParam String username) {
        log.info("랭킹 검색 : {}", username);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberTaskException("존재하지 않는 사용자입니다.", 404));

        return ResponseEntity.ok(RankingResponse.of(
                rankingService.findRankByMemberId(member.getId()),
                username,
                rankingService.findWeeklyPointsByMemberId(member.getId())
        ));
    }

    @GetMapping("/week")
    public ResponseEntity<Page<RankingResponse>> showAllRankings(Pageable pageable) {
        log.info("전체 랭킹 조회");
        return ResponseEntity.ok(rankingService.showAllRankingList(pageable));
    }

    @GetMapping("/friend")
    public ResponseEntity<Page<RankingResponse>> showFriendRankings(@AuthenticationPrincipal SecurityUser securityUser,
                                                                    Pageable pageable) {
        log.info("친구 랭킹 조회");
        if (securityUser == null) {
            throw new MemberTaskException("인증 정보가 존재하지 않습니다.", 401);
        }
        return ResponseEntity.ok(rankingService.showFriendRankingList(securityUser.getId(), pageable));
    }
}
