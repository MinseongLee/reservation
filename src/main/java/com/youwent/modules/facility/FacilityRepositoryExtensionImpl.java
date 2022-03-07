package com.youwent.modules.facility;

import com.querydsl.jpa.JPQLQuery;
import com.youwent.modules.account.Account;
import com.youwent.modules.reservation.QReservation;
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
        QReservation reservation = QReservation.reservation;
        // account 이면서, status=True 인 애들을 제외하고 리턴. : 이 값에 속하는 facility들이 속하면 안 된다.
        JPQLQuery<Facility> facilities = from(reservation).where(reservation.account.eq(account), reservation.status.isTrue()).select(reservation.facility);
        return from(facility).where(facility.notIn(facilities));
    }
}
