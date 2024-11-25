package com.prgrms.ijuju.domain.point.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.NotNull;

import com.prgrms.ijuju.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PointDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @NotNull
    private Long pointAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private String detailType;
}
