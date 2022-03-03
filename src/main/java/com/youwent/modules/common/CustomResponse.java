package com.youwent.modules.common;

import lombok.*;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class CustomResponse {

    private String statusCode;

    private String resultMsg;

    public static CustomResponse defaultCustomResponse() {
        return CustomResponse.builder()
                .statusCode("200")
                .resultMsg("success")
                .build();
    }
}
