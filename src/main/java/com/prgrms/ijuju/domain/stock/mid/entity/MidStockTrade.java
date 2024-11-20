package com.prgrms.ijuju.domain.stock.mid.entity;

import com.prgrms.ijuju.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
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

//    멤버 엔티티가 생성될시 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;


    public MidStockTrade(Long id, long tradePoint, long pricePerStock, TradeType tradeType, MidStock midStock) {
        this.id = id;
        this.tradePoint = tradePoint;
        this.pricePerStock = pricePerStock;
        this.tradeType = tradeType;
        if (midStock != null) {
            setMidStock(midStock);
        }
    }

    public void setMidStock(MidStock midStock) {
        this.midStock = midStock;
        midStock.getTrades().add(this);
    }
}
