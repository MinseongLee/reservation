package com.youwent.controller;


import com.youwent.entity.account.Account;
import com.youwent.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;

    // 스프링 시큐리티를 사용하면 mockMvc사용 방법도 달라진다. 더 많은 기능을 지원

    @DisplayName("회원 가입 화면 테스트")
    @Test
    void signup() throws Exception {
        mockMvc.perform(get(SIGNUP))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT + SIGNUP))
                .andExpect(model().attributeExists("accountDto"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpSubmit_wrong_input() throws Exception {
        // 403 에러가 났다. 왜냐하면 get요청을 하고 폼 데이터를 받을 때,
        // csrf 토큰도 함꼐 받는데, 그 토큰을 받지 못해서 유효하지 않다는 에러가 뜨는 것이다.
        mockMvc.perform(post(SIGNUP)
                .param("name", "a")
                .param("email", "ema")
                .param("password", "12")
                .param("phone", "01203")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT + SIGNUP))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_correct_input() throws Exception {
        // 403 에러가 났다. 왜냐하면 get요청을 하고 폼 데이터를 받을 때,
        // csrf 토큰도 함꼐 받는데, 그 토큰을 받지 못해서 유효하지 않다는 에러가 뜨는 것이다.
        mockMvc.perform(post(SIGNUP)
                        .param("name", "dexter3")
                        .param("email", "dexlee45@gmail.com")
                        .param("password", "dexter123")
                        .param("phone", "01057642433")
                        //form 데이터를 보낼 때에는 csrf 토큰을 항상 같이 보내야한다.
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + ROOT))
                .andExpect(authenticated().withUsername("dexlee45@gmail.com"));

        Account account = accountRepository.findByEmail("dexlee45@gmail.com");
        assertNotNull(account);
        assertNotNull(account.getEmailCheckToken());
        assertNotEquals(account.getPassword(), "dexter123");
    }

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @Test
    void checkEmailToken_wrong_input() throws Exception {
        mockMvc.perform(get(EMAILTOKEN)
                .param("token", "dfwds")
                .param("email", "dexter@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checkedemail"))
                .andExpect(unauthenticated());
    }

    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test
    void checkEmailToken_correct_input() throws Exception {
        Account account = Account.builder()
                        .email("test@gmail.com")
                        .password("dexter123")
                        .name("dexter")
                        .phone("01057642432")
                        .build();
        account.generateEmailCheckToken();
        Account newAccount = accountRepository.save(account);

        mockMvc.perform(get(EMAILTOKEN)
                        .param("token", newAccount.getEmailCheckToken())
                        .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("name"))
                .andExpect(view().name("account/checkedemail"))
                .andExpect(authenticated());
    }



}
