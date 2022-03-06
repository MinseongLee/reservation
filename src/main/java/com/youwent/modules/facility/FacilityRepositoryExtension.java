package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface FacilityRepositoryExtension {
    // account id가 속한 모든 reservation 제외 (status=true)
    List<Facility> findByAllExceptForReservation(Account account);

    List<Facility> findByKeywordOrderByAsc(Account account, String keyword);
    List<Facility> findByKeywordOrderByDesc(Account account, String keyword);
}
