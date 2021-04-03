package com.memorytrace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@IdClass(UserBookPK.class)
@Table(name = "user_book")
public class UserBook {

    @Id
    @Column(name = "uid", insertable = false, updatable = false)
    private long uid;

    @Id
    @Column(name = "bid", insertable = false, updatable = false)
    private long bid;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid", insertable = false, updatable = false)
    private Book book;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long turnNo;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private byte isWithdrawal;

    @Builder
    public UserBook(long uid, long bid, long turnNo, byte isWithdrawal) {
        this.uid = uid;
        this.bid = bid;
        this.turnNo = turnNo;
        this.isWithdrawal = isWithdrawal;
    }
}
