package com.memorytrace.domain;

import java.util.UUID;
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
import org.hibernate.annotations.GenericGenerator;

@Getter
@NoArgsConstructor
@Entity
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bid;

    @ManyToOne
    @JoinColumn(name = "whose_turn", referencedColumnName = "uid")
    private User user;

    @Column(length = 256, nullable = false)
    private String title;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private byte bgColor;

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID inviteCode;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private byte isDelete;

    @Builder
    public Book(User user, String title, byte bgColor, byte isDelete) {
        this.user = user;
        this.title = title;
        this.bgColor = bgColor;
        this.isDelete = isDelete;
    }
}
