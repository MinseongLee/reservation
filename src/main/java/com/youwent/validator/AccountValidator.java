package com.youwent.validator;


import com.youwent.entity.account.AccountDto;
import com.youwent.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
// 필요한 생성자를 만들어줌. (생성자 DI)
@RequiredArgsConstructor
public class AccountValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDto accountDto = (AccountDto) target;
        if (accountRepository.existsByEmail(accountDto.getEmail())){
            errors.rejectValue("email", "invalid.email", new Object[]{accountDto.getEmail()}, "이미 사용중인 이메일이 존재합니다.");
        }

    }
}
