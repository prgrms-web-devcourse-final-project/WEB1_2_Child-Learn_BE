package com.prgrms.ijuju.domain.ranking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RankingResponse {
    private final Long rank;
    private final String username;
    private final Long weeklyPoints;

    @Builder
    public RankingResponse(Long rank, String username, Long weeklyPoints) {
        this.rank = rank;
        this.username = username;
        this.weeklyPoints = weeklyPoints;
    }

    public static RankingResponse of(Long rank, String username, Long weeklyPoints) {
        return RankingResponse.builder()
                .rank(rank)
                .username(username)
                .weeklyPoints(weeklyPoints)
                .build();
    }
}
