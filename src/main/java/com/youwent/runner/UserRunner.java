package com.youwent.runner;


import com.youwent.modules.account.AccountRepository;
import com.youwent.modules.account.AccountService;
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

        // 일반 유저
//        AccountDto user = new AccountDto();
//        user.setEmail("diakswl11@gmail.com");
//        user.setName("dexter");
//        user.setPhone("01057642424");
//        user.setPassword("dexter1234");
//        Account account = accountService.processNewAccount(user);
//        accountService.login(account);
    }
}
