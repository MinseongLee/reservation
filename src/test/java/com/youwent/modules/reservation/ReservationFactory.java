package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.facility.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class ReservationFactory {

    @Autowired
    private final ReservationRepository reservationRepository;

    public Reservation createReservation(Account account, Facility facility) {
        Reservation reservation = Reservation.builder()
                .reservedDate(LocalDateTime.now())
                .status(true)
                .createdDate(LocalDateTime.now())
                .account(account)
                .facility(facility)
                .build();
        reservationRepository.save(reservation);
        return reservation;
    }
}
