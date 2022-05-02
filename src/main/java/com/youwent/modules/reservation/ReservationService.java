package com.youwent.modules.reservation;


import com.youwent.modules.account.Account;
import com.youwent.modules.reservation.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public void updateStatus(Reservation reservation) {
        reservation.updateStatus();
    }

    public Reservation getReservation(Long id) {
        // 여기에서 findById로 가져오면 eager이라면 그 즉시 연관관계 데이터가 항상 같이 조회된다.
        // 이 문제를 해결하려면 lazy로 놓고, 해당하는 값을 가져올 때 직접 조인을 통해 가져와야한다.
        Optional<Reservation> reservationById = reservationRepository.findById(id);
        if (!reservationById.isPresent()) {
            throw new IllegalArgumentException("해당하는 예약이 존재하지 않습니다.");
        }
        return reservationById.get();

    }

    public Page<ReservationDto> getReservationsByKeyword(Account account, String keyword, String orderByBuilding, Pageable pageable) {
        return orderByBuilding.equals("asc") ? reservationRepository.findByKeywordOrderByAsc(account, keyword, pageable) : reservationRepository.findByKeywordOrderByDesc(account, keyword, pageable);
    }

    public String getKeyword(String keyword) {
        return keyword == null ? "" : keyword;
    }

    public String getOrderByBuilding(String orderByBuilding) {
        return orderByBuilding == null ? "" : orderByBuilding;
    }
}
