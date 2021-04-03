package com.memorytrace.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBookPK implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long uid;
    private Long bid;
}
