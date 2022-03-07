package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.facility.Facility;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface ReservationRepositoryExtension {
    List<Reservation> findByAccountAndStatusIsTrue(Account account);

    List<Reservation> findByKeywordOrderByAsc(Account account,String keyword);
    List<Reservation> findByKeywordOrderByDesc(Account account,String keyword);
    Long findByReservationToday(Facility facility);
    Long findByReservedDate(Facility facility, LocalDateTime reservedDate);
}
