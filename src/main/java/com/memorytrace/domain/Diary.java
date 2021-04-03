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
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long did;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid")
    private Book book;

    @Column(length = 256)
    private String title;

    @Column(length = 256)
    private String img;

    @Column(length = 2000)
    private String content;

    @Column(columnDefinition = "TINYINT")
    private byte weather;

    @Column(columnDefinition = "TINYINT")
    private byte alignment;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private byte bgColor;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private byte template;

    @Builder
    public Diary(User user, Book book, String title, String img, String content, byte weather,
        byte alignment, byte bgColor, byte template) {
        this.user = user;
        this.book = book;
        this.title = title;
        this.img = img;
        this.content = content;
        this.weather = weather;
        this.alignment = alignment;
        this.bgColor = bgColor;
        this.template = template;
    }
}
