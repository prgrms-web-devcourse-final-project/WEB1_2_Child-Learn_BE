package com.prgrms.ijuju.domain.stock.mid.entity;

import com.prgrms.ijuju.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void setMidStock(MidStock midStock) {
        this.midStock = midStock;
        midStock.getStockPrices().add(this);
    }
}
