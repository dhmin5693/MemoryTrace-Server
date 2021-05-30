package com.memorytrace.dto.request;

import com.memorytrace.domain.UserBook;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DiaryExitRequestDto {
    public UserBook toEntity(Long uid, Long bid) {
        return UserBook.builder()
            .uid(uid)
            .bid(bid)
            .isWithdrawal((byte) 1)
            .build();
    }
}
