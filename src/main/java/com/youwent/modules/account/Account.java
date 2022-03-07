package com.youwent.modules.account;

import com.youwent.modules.common.BaseEntity;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.reservation.Reservation;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Account extends BaseEntity {
    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String phone;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime createdDate;

    private LocalDateTime emailCheckTokenCreatedDate;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenCreatedDate = LocalDateTime.now();
    }

    public void completeSignup() {
        this.emailVerified = true;
        this.createdDate = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenCreatedDate.isBefore(LocalDateTime.now().minusMinutes(5));
    }

    @Builder
    public Account(String email, String password, String name, UserType userType, String phone, boolean emailVerified, String emailCheckToken, LocalDateTime createdDate, LocalDateTime emailCheckTokenCreatedDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.phone = phone;
        this.emailVerified = emailVerified;
        this.emailCheckToken = emailCheckToken;
        this.createdDate = createdDate;
        this.emailCheckTokenCreatedDate = emailCheckTokenCreatedDate;
    }
}
