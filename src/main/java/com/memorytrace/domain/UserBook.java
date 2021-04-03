package com.memorytrace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_book")
public class UserBook {

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid")
    private Book book;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long turnNo;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private byte isWithdrawal;

    @Builder
    public UserBook(User user, Book book, long turnNo, byte isWithdrawal) {
        this.user = user;
        this.book = book;
        this.turnNo = turnNo;
        this.isWithdrawal = isWithdrawal;
    }
}
