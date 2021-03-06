package com.memorytrace.domain;

import com.memorytrace.dto.request.BookUpdateRequestDto;
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
    private Byte bgColor;

    @Column(length = 256)
    private String stickerImg;

    @Column(columnDefinition = "VARCHAR(36)")
    private String inviteCode;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private Byte isDelete;

    @Builder(builderClassName = "ByBidBuilder", builderMethodName = "ByBidBuilder")
    public Book(Long bid) {
        this.bid = bid;
    }

    @Builder
    public Book(User user, String title, Byte bgColor, String stickerImg, String inviteCode,
        Byte isDelete) {
        this.user = user;
        this.title = title;
        this.bgColor = bgColor;
        this.stickerImg = stickerImg;
        this.inviteCode = inviteCode;
        this.isDelete = isDelete;
    }

    @Builder(builderClassName = "UpdateBuilder", builderMethodName = "UpdateBuilder")
    public Book(Long bid, User user, String title, Byte bgColor, String stickerImg,
        String inviteCode,
        Byte isDelete) {
        this.bid = bid;
        this.user = user;
        this.title = title;
        this.bgColor = bgColor;
        this.stickerImg = stickerImg;
        this.inviteCode = inviteCode;
        this.isDelete = isDelete;
    }

    public void updateWhoseTurnBook(Long bid, User nextUser) {
        this.bid = bid;
        this.user = nextUser;
    }

    public void update(BookUpdateRequestDto requestDto, String stickerImg) {
        this.title = requestDto.getTitle();
        this.bgColor = requestDto.getBgColor();
        this.stickerImg = stickerImg;
    }

    public void delete() {
        this.isDelete = 1;
        this.inviteCode = null;
    }
}
