package com.prgrms.ijuju.domain.stock.mid.entity;

import com.prgrms.ijuju.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MidStock extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;

    @OneToMany(mappedBy = "midStock", cascade = CascadeType.ALL)
    private List<MidStockPrice> stockPrices = new ArrayList<>();

    @OneToMany(mappedBy = "midStock", cascade = CascadeType.ALL)
    private List<MidStockTrade> trades = new ArrayList<>();
}
