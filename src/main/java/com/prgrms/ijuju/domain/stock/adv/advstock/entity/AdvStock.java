package com.prgrms.ijuju.domain.stock.adv.advstock.entity;

import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = { AuditingEntityListener.class })
public class AdvStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;  //주식 심볼 > 에: "AAPL"
    private String name;    //주식 이름 > 에: "Apple Inc."

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> openPrices;   //시가 'o'

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> highPrices;   //고가 'h'

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> lowPrices;    //저가 'l'

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> closePrices;  //종가 'c'

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> volumes;      //거래량 'v'

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> timestamps;   //시간 't'

    @Enumerated(EnumType.STRING)
    private DataType dataType;  //REFERENCE or LIVE > stock.constant 내부 확인
}