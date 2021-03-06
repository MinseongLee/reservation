package com.youwent.modules.facility;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.youwent.modules.account.Account;
import com.youwent.modules.reservation.QReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class FacilityRepositoryExtensionImpl extends QuerydslRepositorySupport implements FacilityRepositoryExtension {

    public FacilityRepositoryExtensionImpl() {
        super(Facility.class);
    }

    @Override
    public Page<Facility> findByKeywordOrderByAsc(Account account, String keyword, Pageable pageable) {
        QFacility facility = QFacility.facility;
        JPQLQuery<Facility> query = findByAllExceptAccount(account).where(facility.building.containsIgnoreCase(keyword))
                .orderBy(facility.building.asc());
        JPQLQuery<Facility> pageableQuery = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
        QueryResults<Facility> results = pageableQuery.fetchResults();
        // contents, pageable, totalCnt(현재기준)
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Facility> findByKeywordOrderByDesc(Account account, String keyword, Pageable pageable) {
        QFacility facility = QFacility.facility;
        JPQLQuery<Facility> query = findByAllExceptAccount(account).where(facility.building.containsIgnoreCase(keyword))
                .orderBy(facility.building.desc());
        JPQLQuery<Facility> pageableQuery = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
        QueryResults<Facility> results = pageableQuery.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // 오늘 기준으로 예약자 모두 가져올 것. status=true
    @Override
    public List<Facility> findAllWithReservations() {
        QFacility facility = QFacility.facility;
        JPQLQuery<Facility> query = from(facility).where(facility.reservations.any().status.isTrue()
                .and(facility.reservations.any().reservedDate.eq(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0)))))
                .innerJoin(facility.reservations, QReservation.reservation).fetchJoin();
        return query.fetch();
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
