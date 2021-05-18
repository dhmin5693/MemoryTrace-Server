package com.memorytrace.domain;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false, unique = true)
    private String snsKey;

    @Column(length = 256)
    private String profileImg;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private Byte isWithdrawal;

    @Builder(builderClassName = "ByUidBuilder", builderMethodName = "ByUidBuilder")
    public User(Long uid) {
        this.uid = uid;
    }

    @Builder(builderClassName = "ByUserBuilder", builderMethodName = "ByUserBuilder")
    public User(String nickname, String snsKey, String profileImg, Byte isWithdrawal) {
        this.nickname = nickname;
        this.snsKey = snsKey;
        this.profileImg = profileImg;
        this.isWithdrawal = isWithdrawal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return snsKey;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
