package com.youwent.validator;


import com.youwent.entity.account.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//form 입력 단계에서 에러가 발생할 시, 처리
public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmedNewPassword())) {
            errors.rejectValue("newPassword", "wrong.value", "입력한 패스워드가 일치하지 않습니다.");
        }

    }
}
