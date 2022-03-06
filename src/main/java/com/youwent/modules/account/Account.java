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

//    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
//    private Set<Reservation> reservations = new HashSet<>();

//    @OneToMany(mappedBy = "account")
//    private Set<AccountFacility> account_facilities = new HashSet<>();

    // account를 넣으려 할 때, account를 매번 detached에서 persist로 변경하기 보단 eager로 처리하는게 더 효율적이라 판단
//    @ManyToMany(mappedBy = "accounts", fetch = FetchType.EAGER)
//    private Set<Facility> facilities = new HashSet<>();

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

//    public boolean isAdmin() {
//        return this.userType == UserType.ADMIN;
//    }
}
