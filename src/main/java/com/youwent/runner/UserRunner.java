package com.youwent.runner;


import com.youwent.modules.account.Account;
import com.youwent.modules.account.AccountRepository;
import com.youwent.modules.account.AccountService;
import com.youwent.modules.account.UserType;
import com.youwent.modules.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        admin.setEmail("dexlee@deali.net");
//        admin.setPassword("dexter1234");
//        admin.setName("dexter");
//        admin.setPhone("01022223333");
//        accountService.login(accountService.processNewAccount(admin));
//        Account byEmail = accountRepository.findByEmail("dexlee@deali.net");
//        byEmail.setUserType(UserType.ADMIN);
    }
}
