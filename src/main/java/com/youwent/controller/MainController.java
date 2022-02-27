package com.youwent.controller;

import com.youwent.entity.account.Account;
import com.youwent.entity.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private static final String INDEX = "index";
    private static final String LOGIN = "login";

    @GetMapping("/")
    public String index(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return INDEX;
    }

    @GetMapping("login")
    public String login() {
        return LOGIN;
    }
}
