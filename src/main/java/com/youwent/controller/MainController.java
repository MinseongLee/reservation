package com.youwent.controller;

import com.youwent.entity.account.Account;
import com.youwent.entity.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.youwent.model.enumTypes.Url.*;

@Controller
@RequestMapping(ROOT)
public class MainController {

    @GetMapping
    public String index(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return INDEX;
    }

    @GetMapping(LOGIN)
    public String login() {
        return LOGIN;
    }
}
