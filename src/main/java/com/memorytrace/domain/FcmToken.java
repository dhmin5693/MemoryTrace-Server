package com.memorytrace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    @Column(length = 256)
    private String token;

    @Builder
    public FcmToken(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
