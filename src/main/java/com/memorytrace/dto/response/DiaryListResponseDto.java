package com.memorytrace.dto.response;

import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "DiaryList 조회 요청")
public class DiaryListResponseDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long did;

    @Column(length = 256)
    private String title;

    @Column(length = 256)
    private String img;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Byte bgColor;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Byte template;

    public DiaryListResponseDto(Diary entity) {
        this.did = entity.getDid();
        this.title = entity.getTitle();
        this.img = entity.getImg();
        this.bgColor = entity.getBgColor();
        this.template = entity.getTemplate();
    }
}
