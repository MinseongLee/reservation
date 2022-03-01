package com.youwent.controller;


import com.youwent.entity.account.AccountDto;
import com.youwent.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.youwent.model.enumTypes.Url.*;

@SpringBootTest
@AutoConfigureMockMvc
// 이걸로는 스프링이 주입을 못해준다. 왜냐하면 junit이 먼저 생성자에 접근을 하므로,
//@RequiredArgsConstructor
public class MainControllerTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void beforeEach() {
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail("dexter@gmail.com");
        accountDto.setName("dexter");
        accountDto.setPassword("dexter1234");
        accountDto.setPhone("01055552222");
        accountService.processNewAccount(accountDto);
    }

    @DisplayName("이메일 로그인 성공")
    @Test
    void login_email() throws Exception {
        mockMvc.perform(post(ROOT+LOGIN)
                .param("username", "dexter@gmail.com")
                .param("password", "dexter1234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT))
                .andExpect(authenticated().withUsername("dexter@gmail.com"));
    }

    @DisplayName("이메일 로그인 실패")
    @Test
    void login_fail_email() throws Exception {
        mockMvc.perform(post(ROOT + LOGIN)
                        .param("username", "deer@gmail.com")
                        .param("password", "dxter1234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + LOGIN + "?error"))
                .andExpect(unauthenticated());
    }

    // 유저를 하나 생성해줌.
    @WithMockUser
    @DisplayName("logout")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT))
                .andExpect(unauthenticated());

    }


}
