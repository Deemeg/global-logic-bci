package com.globallogic.bcigloballogic.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private Timestamp timestamp;
    private Integer code;
    private String detail;
}
