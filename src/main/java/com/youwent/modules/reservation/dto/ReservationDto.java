package com.youwent.modules.reservation.dto;

import com.youwent.modules.reservation.Reservation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDto {
    private Long id;

    private String building;

    private String address;

    private String openTime;

    private String closeTime;

    private String reservedDate;

    public static ReservationDto createReservationDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setBuilding(reservation.getFacility().getBuilding());
        reservationDto.setAddress(reservation.getFacility().getAddress());
        reservationDto.setOpenTime(reservation.getFacility().getOpenTime().toString());
        reservationDto.setCloseTime(reservation.getFacility().getCloseTime().toString());
        reservationDto.setReservedDate(reservation.getReservedDate().toString());
        return reservationDto;
    }

}
