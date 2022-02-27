package com.youwent.controller;

import com.youwent.entity.account.Account;
import com.youwent.entity.account.AccountDto;
import com.youwent.entity.account.CurrentAccount;
import com.youwent.repository.AccountRepository;
import com.youwent.service.AccountService;
import com.youwent.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private static final String ACCOUNT_SIGNUP = "account/signup";
    private static final String ACCOUNT_CHECKEDEMAIL = "account/checkedEmail";
    private static final String ACCOUNT_CHECKEMAIL = "account/checkEmail";
    private static final String ACCOUNT_PROFILE = "account/profile";


    private final AccountValidator accountValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder("accountDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountValidator);
    }


    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute(new AccountDto());
        return ACCOUNT_SIGNUP;
    }

    // @ModelAttribute accountDto : 여기서 앞에 애노테이션은 생략 가능
    @PostMapping("/signup")
    public String createAccount(@Valid AccountDto accountDto, Errors errors) {
        if (errors.hasErrors()) {
            return ACCOUNT_SIGNUP;
        }

        Account account = accountService.processNewAccount(accountDto);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/emailtoken")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return ACCOUNT_CHECKEDEMAIL;
        }

        // 읽기가 쉽지 않은 논리연산은 가독성을 위해 함수로 빼놓을 것.
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return ACCOUNT_CHECKEDEMAIL;
        }

        accountService.completeSignUp(account);
        model.addAttribute("name", account.getName());
        return ACCOUNT_CHECKEDEMAIL;
    }

    @GetMapping("/checkemail")
    public String checkEmail(@CurrentAccount Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return ACCOUNT_CHECKEMAIL;
    }

    @GetMapping("/resendemail")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model) {
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 5분에 한 번만 전송할 수 있습니다.");
            model.addAttribute("email", account.getEmail());
            return ACCOUNT_CHECKEMAIL;
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{id}")
    public String accountDetail(@PathVariable Long id, Model model, @CurrentAccount Account account) {
        Optional<Account> accountById = accountRepository.findById(id);
        if (!accountById.isPresent()) {
            throw new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다.");
        }

        // account를 줘야한다.
        model.addAttribute("account", accountById.get());
        // 현재 접속한 계정의 오너인지
        model.addAttribute("isOwner", accountById.get().equals(account));
        return ACCOUNT_PROFILE;
    }

}
