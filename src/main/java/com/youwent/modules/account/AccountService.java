package com.youwent.modules.account;

import com.youwent.infra.config.AppProperties;
import com.youwent.modules.account.dto.AccountDto;
import com.youwent.modules.account.dto.Profile;
import com.youwent.infra.mail.EmailMessage;
import com.youwent.infra.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    // @Transactional 을 붙이면 entity의 detached 상태가 아닌 영속 상태가 이 함수까지 유지가 된다.
    public Account processNewAccount(AccountDto accountDto) {
        Account newAccount = saveAccount(accountDto);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveAccount(@Valid AccountDto accountDto) {
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
        context.setVariable("link", "/emailtoken?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("email", newAccount.getEmail());
        context.setVariable("name", newAccount.getName());
        context.setVariable("linkName", newAccount.getEmail()+" 이메일 인증하기");
        context.setVariable("message", "덱스터예약 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/link", context);


        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("덱스터예약: 이메일 인증")
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

    // account entity가 영속성 상태 (persistent status) : 변경 이력을 추적해줌.
    // 왜냐하면 accountRepository에서 가져온 객체이므로 영속성 상태로 넘어간 상태이다.
    public void completeSignUp(Account account) {
        account.completeSignup();
        login(account);
    }

    // @CurrentAccount 객체는 detached status
    // 그래서 트랜잭션 처리는 되지만 상태 변경은 자동으로 안 된다.
    public void updateProfile(Account account, Profile profile) {
        //profile 프로퍼티들을 모두 account에 매핑해줌.
        modelMapper.map(profile, account);
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    // for admin test
    public void setUserType(Account account) {
        account.setUserType(UserType.ADMIN);
        accountRepository.save(account);
    }
}
