package com.prgrms.ijuju.domain.stock.mid.entity;

import com.prgrms.ijuju.common.BaseTimeEntity;
import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "tradePoint", "pricePerStock", "tradeType"})
public class MidStockTrade extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long tradePoint;

    private long pricePerStock;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_stock_id")
    private MidStock midStock;

    //    멤버 엔티티가 생성될시 추가 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public MidStockTrade(Long id, long tradePoint, long pricePerStock, TradeType tradeType,
                         MidStock midStock, Member member) {
        this.id = id;
        this.tradePoint = tradePoint;
        this.pricePerStock = pricePerStock;
        this.tradeType = tradeType;
        if (midStock != null) {
            setMidStock(midStock);
        }
        this.member = member; // 멤버 설정 추가
    }

    public void setMidStock(MidStock midStock) {
        this.midStock = midStock;
        midStock.getTrades().add(this);
    }

    public void changeTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }
}
