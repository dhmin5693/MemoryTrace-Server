package com.memorytrace.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBookPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private long uid;
    private long bid;
}
