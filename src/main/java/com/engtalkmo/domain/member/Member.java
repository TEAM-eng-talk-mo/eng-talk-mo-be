package com.engtalkmo.domain.member;

import com.engtalkmo.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name = "member", indexes = {
        @Index(name = "idx_member_member_type", columnList = "member_type")
})
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Lob
    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    @Builder
    public Member(String email, String password, String name, String profileImage, Role role, MemberType memberType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.role = role;
        this.memberType = memberType;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Member member = (Member) o;
        return getId() != null && Objects.equals(getId(), member.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
