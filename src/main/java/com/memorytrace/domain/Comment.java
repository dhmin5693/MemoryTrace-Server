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
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private Long parent;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "did", referencedColumnName = "did")
    private Diary diary;

    @Column(length = 2000)
    private String content;

    @Builder
    public Comment(Long cid, Long parent, User user, Diary diary, String content) {
        this.cid = cid;
        this.parent = cid;
        this.user = user;
        this.diary = diary;
        this.content = content;
    }
}
