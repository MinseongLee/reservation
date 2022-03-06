package com.youwent.modules.facility;

import com.youwent.modules.common.BaseEntity;
import com.youwent.modules.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
public class Facility extends BaseEntity {
    /***
     *  시설정보 : 시설명, 주소, open/close 시간
     *  예약상태 : 예약 가능 / 불가, 현재예약수, 최대예약수, 나의 예약 상태
     *
     *  예약 날짜 -
     *  유저 <- 시설 (ntom) - 단방향
     *  유저 <-> 예약 (1ton) - 양방향
     *  시설 <- 예약 (1tom) - 단방향
     */
    private String building;

    private String address;

    private LocalTime openTime;

    private LocalTime closeTime;

    // 이걸 reservedDate랑 LocalDateTime.now()와 같은 수 만큼 숫자를 세야한다.
    // 혹은 선택한 날짜에 reserveDate와 같은 수 만큼 숫자를 출력해야한다.
    private int nowReserveCnt;

    private int maxReserveCnt;

    // default = true
    private boolean possibleReservation;

    private LocalDateTime createdDate;

//    @OneToMany(mappedBy = "facility")
//    private Set<AccountFacility> account_facilities = new HashSet<>();

    // 누가 등록하든 다 보여줘야한다. 그리고 등록자를 나타내줘야한다.
    // 그냥 양방향 매핑을 하자.
    @ManyToMany
    @JoinTable(name = "facility_account",
            joinColumns = { @JoinColumn(name = "facility_id") },
            inverseJoinColumns = { @JoinColumn(name = "account_id") })
    private Set<Account> accounts = new HashSet<>();

    // 지금 여기서.. set을 초기화 했는데도 null이 뜬다. 이유가 뭐지?
    // 아마도.. build를 하면, 해당 내용이 자동으로 추가가 안되는거같다. 생성자에 빌더를 사용하여 직접 입력해서 넣자.
    private void addAccountFacility(Account account) {
        this.accounts.add(account);
//        account.getFacilities().add(this);
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

    public boolean HaveReserve() {
        return this.nowReserveCnt < this.maxReserveCnt;
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
