package com.prgrms.ijuju.domain.avatar.entity;


import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "avatar")
public class Avatar extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // member 객체와 연결
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    private Item background;

    @OneToOne
    private Item pet;

    @OneToOne
    private Item hat;

}
