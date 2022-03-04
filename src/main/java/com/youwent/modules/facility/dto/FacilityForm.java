package com.youwent.modules.facility.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class FacilityForm {
    @NotBlank
    @Length(max = 50)
    private String building;

    @NotBlank
    @Length(max = 100)
    private String address;

    @NotBlank
    @DateTimeFormat(pattern = "HH:mm")
    private String openTime;

    @NotBlank
    @DateTimeFormat(pattern = "HH:mm")
    private String closeTime;

    @Min(value = 1)
    private int maxReserveCnt;
}
