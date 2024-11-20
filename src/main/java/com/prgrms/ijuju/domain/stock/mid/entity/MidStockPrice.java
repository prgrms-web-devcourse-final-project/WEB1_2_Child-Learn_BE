package com.prgrms.ijuju.domain.stock.mid.entity;

import com.prgrms.ijuju.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "highPrice", "lowPrice", "avgPrice", "priceDate"})
public class MidStockPrice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long highPrice;

    private long lowPrice;

    private long avgPrice;

    private LocalDateTime priceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_stock_id")
    private MidStock midStock;

    @Builder
    public MidStockPrice(long highPrice, long lowPrice, long avgPrice, LocalDateTime priceDate, MidStock midStock) {
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.avgPrice = avgPrice;
        this.priceDate = priceDate;
        if (midStock != null) {
            setMidStock(midStock);
        }
    }


    public void setMidStock(MidStock midStock) {
        this.midStock = midStock;
        midStock.getStockPrices().add(this);
    }
}
