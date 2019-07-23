package com.seol.entity.rest;

import lombok.Data;

@Data
public class Meta {
    private Integer total_count;
    private Integer pageable_count;
    private boolean is_end;
}
