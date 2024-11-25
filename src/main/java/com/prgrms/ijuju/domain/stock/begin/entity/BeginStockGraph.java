package com.prgrms.ijuju.domain.stock.begin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="begin_stock_graph")
@Entity
public class BeginStockGraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long beginId;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String tradeDay;

    @Column(nullable = false)
    private LocalDate stockDate;

    @Builder
    public BeginStockGraph(int price, String tradeDay, LocalDate stockDate) {
        this.price = price;
        this.tradeDay = tradeDay;
        this.stockDate = stockDate;
    }
}
