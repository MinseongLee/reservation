package com.youwent.modules.facility;

import com.querydsl.jpa.JPQLQuery;
import com.youwent.modules.account.Account;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class FacilityRepositoryExtensionImpl extends QuerydslRepositorySupport implements FacilityRepositoryExtension {

    public FacilityRepositoryExtensionImpl() {
        super(Facility.class);
    }

    @Override
    public List<Facility> findByAllExceptForReservation(Account account) {
        return findByAllExceptAccount(account).fetch();
    }

    @Override
    public List<Facility> findByKeywordOrderByAsc(Account account, String keyword) {
        QFacility facility = QFacility.facility;
        return findByAllExceptAccount(account).where(facility.building.containsIgnoreCase(keyword))
                .orderBy(facility.building.asc()).fetch();
    }

    @Override
    public List<Facility> findByKeywordOrderByDesc(Account account, String keyword) {
        QFacility facility = QFacility.facility;
        return findByAllExceptAccount(account).where(facility.building.containsIgnoreCase(keyword))
                .orderBy(facility.building.desc()).fetch();
    }

    // common method
    private JPQLQuery<Facility> findByAllExceptAccount(Account account) {
        QFacility facility = QFacility.facility;
        // account and status.isTrue() 제외한 모든 facilities
        return from(facility).where(facility.reservations.any().account.ne(account)
                .or(facility.reservations.any().status.isFalse()));
    }
}
