package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.reservation.dto.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional(readOnly = true)
public interface ReservationRepositoryExtension {
    Page<ReservationDto> findByKeywordOrderByAsc(Account account, String keyword, Pageable pageable);
    Page<ReservationDto> findByKeywordOrderByDesc(Account account, String keyword, Pageable pageable);
    Long findByReservedDate(Facility facility, LocalDateTime reservedDate);
}
