package com.youwent.modules.reservation;

import com.querydsl.jpa.JPQLQuery;
import com.youwent.modules.account.Account;
import com.youwent.modules.account.QAccount;
import com.youwent.modules.facility.QFacility;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ReservationRepositoryExtensionImpl extends QuerydslRepositorySupport implements ReservationRepositoryExtension {

    public ReservationRepositoryExtensionImpl() {
        super(Reservation.class);
    }

    // innerjoin fetchjoin으로 연관된 데이터를 한 번에 가져와 n+1 문제 해결
    @Override
    public List<Reservation> findByAccountAndStatusIsTrue(Account account) {
        return findByAccountAndStatus(account).fetch();
    }

    @Override
    public List<Reservation> findByKeywordOrderByAsc(Account account, String keyword) {
        QReservation reservation = QReservation.reservation;
        return findByAccountAndStatus(account).where(reservation.facility.building.containsIgnoreCase(keyword))
                .orderBy(reservation.facility.building.asc()).fetch();
    }

    @Override
    public List<Reservation> findByKeywordOrderByDesc(Account account, String keyword) {
        QReservation reservation = QReservation.reservation;
        return findByAccountAndStatus(account).where(reservation.facility.building.containsIgnoreCase(keyword))
                .orderBy(reservation.facility.building.asc()).fetch();
    }

    private JPQLQuery<Reservation> findByAccountAndStatus(Account account) {
        QReservation reservation = QReservation.reservation;
        return from(reservation).where(reservation.account.eq(account)
                        .and(reservation.status.isTrue()))
                .innerJoin(reservation.facility, QFacility.facility).fetchJoin()
                .innerJoin(reservation.account, QAccount.account).fetchJoin();
    }
}
