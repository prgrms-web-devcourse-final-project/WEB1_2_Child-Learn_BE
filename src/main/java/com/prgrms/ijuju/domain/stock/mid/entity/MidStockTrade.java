package com.prgrms.ijuju.domain.stock.mid.entity;

import com.prgrms.ijuju.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

//    멤버 엔티티가 생성될시 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    public void setMidStock(MidStock midStock) {
        this.midStock = midStock;
        midStock.getTrades().add(this);
    }
}
