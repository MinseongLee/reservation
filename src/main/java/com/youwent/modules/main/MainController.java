package com.youwent.modules.main;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.youwent.modules.common.Url.*;

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
