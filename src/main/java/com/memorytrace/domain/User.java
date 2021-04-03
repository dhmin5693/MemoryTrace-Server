package com.memorytrace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String snsKey;

    @Column(length = 256)
    private String profileImg;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private byte isWithdrawal;

    @Builder(builderClassName = "ByUidBuilder", builderMethodName = "ByUidBuilder")
    public User(Long uid) {
        this.uid = uid;
    }

    @Builder(builderClassName = "ByUserBuilder", builderMethodName = "ByUserBuilder")
    public User(String nickname, String snsKey, String profileImg, byte isWithdrawal) {
        this.nickname = nickname;
        this.snsKey = snsKey;
        this.profileImg = profileImg;
        this.isWithdrawal = isWithdrawal;
    }
}
