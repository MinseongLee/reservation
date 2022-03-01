package com.youwent.controller;

import com.youwent.entity.account.*;
import com.youwent.repository.AccountRepository;
import com.youwent.service.AccountService;
import com.youwent.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.Optional;

import static com.youwent.model.enumTypes.Url.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT + SETTINGS)
public class SettingsController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }


    @GetMapping(PROFILE)
    public String updateProfileForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS + PROFILE;
    }

    // @Errors : @ModelAttribute의 에러 처리
    @PostMapping(PROFILE)
    public String updateProfile(@CurrentAccount Account account, @Valid Profile profile, Errors errors,
                                Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PROFILE;
        }

        accountService.updateProfile(account, profile);
        // 한 번 사용되고 없어지는 data
        attributes.addFlashAttribute("message", "프로필을 수정하였습니다.");
        return REDIRECT + ROOT + SETTINGS + PROFILE;
    }

    @GetMapping(PASSWORD)
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS + PASSWORD;
    }

    @PostMapping(PASSWORD)
    public String updatePassword(@CurrentAccount Account account, @Valid PasswordForm passwordForm, Errors errors,
                                 Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PASSWORD;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 성공적으로 변경했습니다.");
        return REDIRECT + ROOT + SETTINGS + PASSWORD;
    }

    @GetMapping(ROOT + ACCOUNT)
    public String accountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        return SETTINGS + ROOT + ACCOUNT;
    }

    @DeleteMapping(ROOT + ACCOUNT + ID)
    @ResponseBody
    public Account deleteAccount(@CurrentAccount Account account, @PathVariable Long id) throws IllegalAccessException {
        Optional<Account> accountById = accountRepository.findById(id);
        if (!accountById.isPresent()) {
            throw new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다.");
        }

        if (!account.equals(accountById.get())) {
            throw new IllegalAccessException("해당하는 사용자와 실제 유저와 같지 않습니다.");
        }
        accountService.deleteAccount(accountById.get());
        return new Account();
    }
}
