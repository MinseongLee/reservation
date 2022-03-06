package com.youwent.modules.common;

import com.youwent.modules.facility.dto.FacilityDto;
import lombok.*;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class CustomResponse {

    private String statusCode;

    private String resultMsg;

    private Object dto;

    public static CustomResponse defaultCustomResponse() {
        return CustomResponse.builder()
                .statusCode("200")
                .resultMsg("success")
                .build();
    }
    public static CustomResponse responseWithFacilityDto(FacilityDto facilityDto) {
        return CustomResponse.builder()
                .statusCode("200")
                .resultMsg("success")
                .dto(facilityDto)
                .build();
    }
}
