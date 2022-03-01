package com.youwent.runner;


import com.youwent.entity.account.Account;
import com.youwent.entity.account.AccountDto;
import com.youwent.model.enumTypes.UserType;
import com.youwent.repository.AccountRepository;
import com.youwent.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRunner implements ApplicationRunner {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 어드민 유저
//        AccountDto admin = new AccountDto();
//        admin.setEmail("admin@gmail.com");
//        admin.setPassword("dexter1234");
//        admin.setName("admin");
//        admin.setPhone("01022223333");
//        accountService.processNewAccount(admin);
//        accountService.login(account);
//        Account byEmail = accountRepository.findByEmail("admin@gmail.com");
//        byEmail.setUserType(UserType.ADMIN);

        // 일반 유저
        AccountDto user = new AccountDto();
        user.setEmail("diakswl11@gmail.com");
        user.setName("dexter");
        user.setPhone("01057642424");
        user.setPassword("dexter1234");
        Account account = accountService.processNewAccount(user);
        accountService.login(account);
    }
}
