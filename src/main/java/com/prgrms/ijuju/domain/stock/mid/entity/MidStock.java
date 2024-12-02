package com.prgrms.ijuju.domain.stock.mid.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "stockName"})
public class MidStock extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;

    @OneToMany(mappedBy = "midStock", cascade = CascadeType.ALL)
    private List<MidStockPrice> stockPrices = new ArrayList<>();

    @OneToMany(mappedBy = "midStock", cascade = CascadeType.ALL)
    private List<MidStockTrade> trades = new ArrayList<>();

    public MidStock(String stockName) {
        this.stockName = stockName;
    }
}
