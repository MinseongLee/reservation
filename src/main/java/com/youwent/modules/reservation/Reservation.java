package com.youwent.modules.reservation;

import com.youwent.modules.common.BaseEntity;
import com.youwent.modules.account.Account;
import com.youwent.modules.facility.Facility;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Reservation extends BaseEntity {
    private LocalDateTime reservedDate;

    private boolean status;

    private LocalDateTime createdDate;

    // 시설 id, user id
    @ManyToOne
    private Account account;

    @ManyToOne
    private Facility facility;

    private void addAccount(Account account) {
        this.account = account;
//        this.account.getReservations().add(this);
    }

    private void addFacility(Facility facility) {
        this.facility = facility;
        this.facility.getReservations().add(this);
    }

    @Builder
    public Reservation(LocalDateTime reservedDate, boolean status, LocalDateTime createdDate, Account account, Facility facility) {
        this.reservedDate = reservedDate;
        this.status = status;
        this.createdDate = createdDate;
        if (account != null) addAccount(account);
        if (facility != null) addFacility(facility);
    }

    public void reserve(LocalDateTime reserved) {
        this.reservedDate = reserved;
        this.status = true;
    }

    public void updateStatus() {
        this.status = false;
        this.facility.minusReserveCnt();
    }
}
