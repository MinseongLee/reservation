package com.youwent.service;

import com.youwent.config.AppProperties;
import com.youwent.entity.account.Account;
import com.youwent.entity.account.AccountDto;
import com.youwent.entity.account.UserAccount;
import com.youwent.mail.EmailMessage;
import com.youwent.mail.ConsoleMailSender;
import com.youwent.mail.EmailService;
import com.youwent.model.enumTypes.UserType;
import com.youwent.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.util.Arrays;

// repository는 디폴트로 transaction이 적용되어져 있다.
// service는 이렇게 애노테이션을 넣어서 직접 트랜잭션을 넣어줘서 처리.
// 이렇게 트랜잭션 안에 있는 상태를 영속성 컨텍스트라고 한다.
// 이렇게 트랜잭션을 주게 되면 이 안에서 일어나는 변경은 자동으로 table에 반영이 된다.(save를 하지 않더라도)**
@Transactional
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final ConsoleMailSender consoleMailSender;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    // @Transactional 을 붙이면 entity의 detached 상태가 아닌 영속 상태가 이 함수까지 유지가 된다.
    public Account processNewAccount(AccountDto accountDto) {
        Account newAccount = saveAccount(accountDto);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveAccount(@Valid AccountDto accountDto) {
        // 나중에 모델 맵퍼를 사용
        Account account = Account.builder()
                .email(accountDto.getEmail())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .name(accountDto.getName())
                .phone(accountDto.getPhone())
                .userType(UserType.USER)
                .build();
        // 이메일 생성확인 위한 토큰
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }


    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        Context context = new Context();
        context.setVariable("token", newAccount.getEmailCheckToken());
        context.setVariable("email", newAccount.getEmail());
        context.setVariable("name", newAccount.getName());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "덱스터예약 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost() + "/emailtoken");
        String message = templateEngine.process("mail/link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("[덱스터예약] 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        // Principal 리턴
        return new UserAccount(account);
    }


    public void completeSignUp(Account account) {
        account.completeSignup();
        login(account);
    }
}
