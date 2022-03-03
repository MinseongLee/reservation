package com.youwent.modules.account;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;

@Getter
public class UserAccount extends User {

    private final Account account;

    public UserAccount(Account account) {
        super(account.getEmail(), account.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }
}
