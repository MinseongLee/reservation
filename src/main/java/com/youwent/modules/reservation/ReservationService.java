package com.youwent.modules.reservation;


import com.youwent.modules.account.Account;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.reservation.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;


    public List<Reservation> getReservationsByAccount(Account account) {
        // status true
        return reservationRepository.findByAccountAndStatusIsTrue(account);
    }

    public List<ReservationDto> getReservationDtoList(List<Reservation> reservations) {
        List<ReservationDto> reservationDtoList = new ArrayList<>();
        for (Reservation r : reservations) {
            reservationDtoList.add(ReservationDto.createReservationDto(r));
        }
        return reservationDtoList;
    }

    public void updateStatus(Reservation reservation) {
        reservation.updateStatus();
    }

    public Reservation getReservation(Long id) {
        Optional<Reservation> reservationById = reservationRepository.findById(id);
        if (!reservationById.isPresent()) {
            throw new IllegalArgumentException("해당하는 예약이 존재하지 않습니다.");
        }
        return reservationById.get();

    }

    public List<Reservation> getReservationsByKeyword(Account account, String keyword, String orderByBuilding) {
        return orderByBuilding.equals("asc") ? reservationRepository.findByKeywordOrderByAsc(account, keyword) : reservationRepository.findByKeywordOrderByDesc(account, keyword);
    }
}
