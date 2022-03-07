package com.youwent.modules.reservation;

import com.querydsl.jpa.JPQLQuery;
import com.youwent.modules.account.Account;
import com.youwent.modules.account.QAccount;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.facility.QFacility;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // 오늘 기준으로 예약자 모두 가져올 것. status=true
    @Override
    public Long findByReservationToday(Facility facility) {
        QReservation reservation = QReservation.reservation;
        JPQLQuery<Long> query = from(reservation).where(reservation.facility.eq(facility)
                .and(reservation.status.isTrue())
                .and(reservation.reservedDate.eq(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0)))))
                .select(reservation.facility.count())
                .groupBy(reservation.facility)
                .distinct()
                .innerJoin(reservation.facility, QFacility.facility).fetchJoin();
        return query.fetchOne();
    }

    @Override
    public Long findByReservedDate(Facility facility, LocalDateTime reservedDate) {
        QReservation reservation = QReservation.reservation;
        JPQLQuery<Long> query = from(reservation).where(reservation.reservedDate.eq(reservedDate)
                        .and(reservation.facility.eq(facility)))
                        .select(reservation.facility.count())
                        .groupBy(reservation.facility);
        return query.fetchOne();
    }

    private JPQLQuery<Reservation> findByAccountAndStatus(Account account) {
        QReservation reservation = QReservation.reservation;
        return from(reservation).where(reservation.account.eq(account)
                        .and(reservation.status.isTrue()))
                .innerJoin(reservation.facility, QFacility.facility).fetchJoin()
                .innerJoin(reservation.account, QAccount.account).fetchJoin();
    }
}
