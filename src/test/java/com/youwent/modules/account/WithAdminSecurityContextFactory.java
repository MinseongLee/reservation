package com.youwent.modules.account;

import com.youwent.modules.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAdminSecurityContextFactory implements WithSecurityContextFactory<WithAdmin> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAdmin withAdmin) {
        String email = withAdmin.value();

        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(email);
        accountDto.setName("dexter");
        accountDto.setPassword("dexter1234");
        accountDto.setPhone("12312312345");
        Account account = accountService.processNewAccount(accountDto);
        accountService.setUserType(account);

        UserDetails principal = accountService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
