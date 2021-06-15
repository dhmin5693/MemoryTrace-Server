package com.memorytrace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@IdClass(UserBookPK.class)
@Table(name = "user_book")
public class UserBook {

    @Id
    @Column(name = "uid", insertable = false, updatable = false)
    private Long uid;

    @Id
    @Column(name = "bid", insertable = false, updatable = false)
    private Long bid;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid", insertable = false, updatable = false)
    private Book book;

    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer turnNo;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private Byte isWithdrawal;

    @Builder
    public UserBook(Long uid, Long bid, Integer turnNo, Byte isWithdrawal, int restOfPeopleCnt) {
        this.uid = uid;
        this.bid = bid;
        this.turnNo = turnNo;
//        this.restOfPeopleCnt = restOfPeopleCnt;
        this.isWithdrawal = isWithdrawal;
    }

//    public void setRestOfPeopleCnt(int restOfPeopleCnt) {
//        this.restOfPeopleCnt = restOfPeopleCnt;
//    }

//    public int getRestOfPeopleCnt() {
//        return restOfPeopleCnt;
//    }

    public void exit() {
        this.isWithdrawal = 1;
    }
}
