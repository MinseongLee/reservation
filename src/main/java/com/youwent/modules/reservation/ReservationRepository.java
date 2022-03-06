package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.facility.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findFirstByAccount_IdAndFacility_Id(Long account_id, Long facility_id);
    List<Reservation> deleteByFacility(Facility facility);
    List<Reservation> deleteByAccount(Account account);
    List<Reservation> findByAccountAndStatusIsTrue(Account account);
}
