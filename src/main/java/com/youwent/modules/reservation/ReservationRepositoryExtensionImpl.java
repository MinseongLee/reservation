package com.youwent.modules.reservation;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.youwent.modules.account.Account;
import com.youwent.modules.account.QAccount;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.facility.QFacility;
import com.youwent.modules.reservation.dto.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationRepositoryExtensionImpl extends QuerydslRepositorySupport implements ReservationRepositoryExtension {

    public ReservationRepositoryExtensionImpl() {
        super(Reservation.class);
    }

    @Override
    public Page<ReservationDto> findByKeywordOrderByAsc(Account account, String keyword, Pageable pageable) {
        QReservation reservation = QReservation.reservation;
        JPQLQuery<ReservationDto> query = findByAccountAndStatus(account).where(reservation.facility.building.containsIgnoreCase(keyword))
                .orderBy(reservation.facility.building.asc())
                .select(Projections.fields(ReservationDto.class,
                        reservation.id,
                        reservation.reservedDate,
                        reservation.facility.building,
                        reservation.facility.address,
                        reservation.facility.openTime,
                        reservation.facility.closeTime));
        JPQLQuery<ReservationDto> pageableQuery = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
        QueryResults<ReservationDto> results = pageableQuery.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // select에 연관관계 데이터가 포함되어 한 번에 가져오면, fetchJoin()을 안써도 n+1 문제가 발생하지 않는다.(데이터를 한 번에 가져오므로)
    @Override
    public Page<ReservationDto> findByKeywordOrderByDesc(Account account, String keyword, Pageable pageable) {
        QReservation reservation = QReservation.reservation;
        JPQLQuery<ReservationDto> query = findByAccountAndStatus(account).where(reservation.facility.building.containsIgnoreCase(keyword))
                .orderBy(reservation.facility.building.desc())
                .select(Projections.fields(ReservationDto.class,
                        reservation.id,
                        reservation.reservedDate,
                        reservation.facility.building,
                        reservation.facility.address,
                        reservation.facility.openTime,
                        reservation.facility.closeTime));
        JPQLQuery<ReservationDto> pageableQuery = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
        QueryResults<ReservationDto> results = pageableQuery.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
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
                .innerJoin(reservation.facility, QFacility.facility)
                .innerJoin(reservation.account, QAccount.account);
    }
}
