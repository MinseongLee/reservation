package com.youwent.controller;

import com.youwent.account.WithAccount;
import com.youwent.entity.account.Account;
import com.youwent.repository.AccountRepository;
import com.youwent.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.youwent.model.enumTypes.Url.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SettingsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach() {
        // 테스트하기 위해 로그인 객체를 직접 만들었기때문에, 지워줘야한다.
        accountRepository.deleteAll();
    }

    // 시큐리티 유저 객체를 직접 만들어서 주입했다. 그러므로, 테스트가 끝난 후 afterEach로 지워주는 것이다.
    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile_form() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + PROFILE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String name = "dexter2";
        String phone = "01033334567";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                .param("name", name)
                .param("phone", phone)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PROFILE))
                .andExpect(flash().attributeExists("message"));

        Account dexter = accountRepository.findByEmail("dexter@gmail.com");
        assertEquals(name, dexter.getName());
        assertEquals(phone, dexter.getPhone());
    }

    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String name = "dexter2wefewfdsdfe";
        String phone = "0103333456732";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                        .param("name", name)
                        .param("phone", phone)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PROFILE))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account dexter = accountRepository.findByEmail("dexter@gmail.com");
        assertNotEquals(name, dexter.getName());
        assertNotEquals(phone, dexter.getPhone());
    }

    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("프로필 수정하기 - 패스워드 수정 폼")
    @Test
    void updatePassword_form() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("프로필 수정하기 - 패스워드 수정 입력값 정상")
    @Test
    void updatePassword() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                .param("newPassword", "dexter12345")
                .param("confirmedNewPassword", "dexter12345")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PASSWORD))
                .andExpect(flash().attributeExists("message"));

        Account dexter = accountRepository.findByEmail("dexter@gmail.com");
        assertTrue(passwordEncoder.matches("dexter12345", dexter.getPassword()));
    }

    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("프로필 수정하기 - 패스워드 수정 입력값 에러")
    @Test
    void updatePassword_errors() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                        .param("newPassword", "dexter12345")
                        .param("confirmedNewPassword", "dexter1234")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PASSWORD));

        Account dexter = accountRepository.findByEmail("dexter@gmail.com");
        assertFalse(passwordEncoder.matches("dexter12345", dexter.getPassword()));
    }

    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("유저 삭제하기")
    @Test
    void deleteUser() throws Exception {
        Account dexter = accountRepository.findByEmail("dexter@gmail.com");
        mockMvc.perform(delete(ROOT + SETTINGS + ROOT + ACCOUNT + ROOT + dexter.getId())
                        .queryParam("id", dexter.getId().toString())
                        .with(csrf()))
                .andExpect(status().isOk());

        Account destroy = accountRepository.findByEmail("dexter@gmail.com");
        assertNull(destroy);
    }






}
