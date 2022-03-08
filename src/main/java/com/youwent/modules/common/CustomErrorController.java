package com.youwent.modules.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {
    @ExceptionHandler(Throwable.class)
    public String exception(Throwable throwable, Model model) {
        String message = throwable == null ? "확인되지 않은 에러입니다." : throwable.getMessage();
        model.addAttribute("errorMessage", message);
        return "error";
    }
}
