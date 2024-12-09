package com.prgrms.ijuju.domain.stock.mid.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "highPrice", "lowPrice", "avgPrice", "priceDate"})
public class MidStockPrice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long highPrice;

    private Long lowPrice;

    private Long avgPrice;

    private LocalDateTime priceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_stock_id")
    private MidStock midStock;

    @Builder
    public MidStockPrice(Long highPrice, Long lowPrice, Long avgPrice, LocalDateTime priceDate, MidStock midStock) {
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
