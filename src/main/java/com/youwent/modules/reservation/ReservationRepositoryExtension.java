package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ReservationRepositoryExtension {
    List<Reservation> findByAccountAndStatusIsTrue(Account account);

    List<Reservation> findByKeywordOrderByAsc(Account account,String keyword);
    List<Reservation> findByKeywordOrderByDesc(Account account,String keyword);
}
