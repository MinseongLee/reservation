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
@Builder @AllArgsConstructor @NoArgsConstructor
public class Reservation extends BaseEntity {
    private LocalDateTime reservedDate;

    private boolean status;

    private LocalDateTime createdDate;

    // 시설 id, user id
    @ManyToOne
    private Account account;

    @ManyToOne
    private Facility facility;

}
