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

@Getter
@NoArgsConstructor
@Entity
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @ManyToOne
    @JoinColumn(name = "whose_turn", referencedColumnName = "uid")
    private User user;

    @Column(length = 256, nullable = false)
    private String title;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private byte bgColor;

    @Column(columnDefinition = "VARCHAR(36)")
    private String inviteCode;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private byte isDelete;

    @Builder
    public Book(User user, String title, byte bgColor, String inviteCode, byte isDelete) {
        this.user = user;
        this.title = title;
        this.bgColor = bgColor;
        this.inviteCode = inviteCode;
        this.isDelete = isDelete;
    }
}
