package com.youwent.modules.facility;


import com.youwent.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Facility findByBuilding(String building);
    List<Facility> findByAccounts(Account account);
}
