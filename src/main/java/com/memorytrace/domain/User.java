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
    private long uid;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String snsKey;

    @Column(length = 256)
    private String profileImg;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private byte isWithdrawal;

    @Builder
    public User(String nickname, String sns_key, String profile_img, byte is_withdrawal) {
        this.nickname = nickname;
        this.snsKey = sns_key;
        this.profileImg = profile_img;
        this.isWithdrawal = is_withdrawal;
    }
}
