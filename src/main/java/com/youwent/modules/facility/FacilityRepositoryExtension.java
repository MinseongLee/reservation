package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface FacilityRepositoryExtension {
    // account id가 속한 모든 reservation 제외 (status=true)
    Page<Facility> findByKeywordOrderByAsc(Account account, String keyword, Pageable pageable);
    Page<Facility> findByKeywordOrderByDesc(Account account, String keyword, Pageable pageable);
    List<Facility> findAllWithReservations();

}
