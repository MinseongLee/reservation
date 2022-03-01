package com.youwent.entity.account;

import com.youwent.model.BaseEntity;
import com.youwent.model.enumTypes.UserType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
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
}
