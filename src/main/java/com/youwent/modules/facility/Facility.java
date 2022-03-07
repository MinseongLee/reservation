package com.youwent.modules.facility;

import com.youwent.modules.common.BaseEntity;
import com.youwent.modules.account.Account;
import com.youwent.modules.reservation.Reservation;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Facility extends BaseEntity {
    private String building;

    private String address;

    private LocalTime openTime;

    private LocalTime closeTime;

    // 오늘 예약수
    private int nowReserveCnt;

    private int maxReserveCnt;

    // default = true
    private boolean possibleReservation;

    private LocalDateTime createdDate;

    // 누가 등록하든 다 보여줘야한다. 그리고 등록자를 나타내줘야한다.
    // 그냥 양방향 매핑을 하자.
    @ManyToMany
    @JoinTable(name = "facility_account",
            joinColumns = { @JoinColumn(name = "facility_id") },
            inverseJoinColumns = { @JoinColumn(name = "account_id") })
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "facility")
    private Set<Reservation> reservations = new HashSet<>();


    private void addAccountFacility(Account account) {
        this.accounts.add(account);
    }
    @Builder
    public Facility(String building, String address, LocalTime openTime, LocalTime closeTime, int nowReserveCnt, int maxReserveCnt, boolean possibleReservation, LocalDateTime createdDate, Account account) {
        this.building = building;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.nowReserveCnt = nowReserveCnt;
        this.maxReserveCnt = maxReserveCnt;
        this.possibleReservation = possibleReservation;
        this.createdDate = createdDate;
        if (account != null) addAccountFacility(account);
    }

    // thread safe
    public synchronized void plusNowReserveCnt() {
        this.nowReserveCnt++;
        checkPossibleReservation();
    }

    public void minusReserveCnt() {
        this.nowReserveCnt--;
    }

    private void checkPossibleReservation() {
        if (this.nowReserveCnt==this.maxReserveCnt) this.possibleReservation = false;
    }
}
