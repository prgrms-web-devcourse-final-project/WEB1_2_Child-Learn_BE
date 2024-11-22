package com.prgrms.ijuju.domain.point.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.ijuju.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    private Long currentPoints;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addPoints(Long points) {
        this.currentPoints += points;
    }

    public void deductPoints(Long points) {
        this.currentPoints -= points;
    }

}
