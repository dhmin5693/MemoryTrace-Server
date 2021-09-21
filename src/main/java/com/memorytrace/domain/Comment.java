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

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private Byte isDelete;

    @Builder
    public Comment(Long parent, User user, Diary diary, String content, Byte isDelete) {
        this.parent = parent;
        this.user = user;
        this.diary = diary;
        this.content = content;
        this.isDelete = isDelete;
    }

    public void delete() {
        this.content = "삭제된 댓글입니다.";
        this.isDelete = 1;
    }
}
