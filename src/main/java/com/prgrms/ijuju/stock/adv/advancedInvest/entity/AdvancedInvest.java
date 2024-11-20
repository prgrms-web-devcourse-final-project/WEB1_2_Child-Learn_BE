package com.prgrms.ijuju.stock.adv.advancedInvest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
@EntityListeners(value = { AuditingEntityListener.class })
public class AdvancedInvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String name;

    @ElementCollection
    private List<Double> openPrices;

    @ElementCollection
    private List<Double> highPrices;

    @ElementCollection
    private List<Double> lowPrices;

    @ElementCollection
    private List<Double> closePrices;

    @ElementCollection
    private List<Double> volumes;

    @ElementCollection
    private List<Double> timestamps;

    private String dataType;
}
