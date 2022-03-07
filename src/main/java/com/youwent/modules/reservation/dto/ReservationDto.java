package com.youwent.modules.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ReservationDto {
    private Long id;

    private String building;

    private String address;

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalDateTime reservedDate;
}
