package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.WithAccount;
import com.youwent.modules.account.WithAdmin;
import com.youwent.infra.MockMvcTest;
import com.youwent.modules.account.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.youwent.modules.common.Url.*;

@MockMvcTest
public class FacilitySettingsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FacilityFactory facilityFactory;
    @Autowired
    private FacilityRepository facilityRepository;

    @AfterEach
    void afterEach() { accountRepository.deleteAll(); }

    @Test
    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 수정 폼")
    void updateFacilityForm() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        mockMvc.perform(get(ROOT + FACILITY + "/" + facility.getId() + ROOT + SETTINGS + ROOT + FACILITY))
                .andExpect(status().isOk())
                .andExpect(view().name(FACILITY + ROOT + SETTINGS + ROOT + FACILITY))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("facility"))
                .andExpect(model().attributeExists("facilityForm"));
    }

    @Test
    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 수정 폼 - 입력 값 정상")
    void updateFacility_input_success() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        String url = ROOT + FACILITY + "/" + facility.getId() + ROOT + SETTINGS + ROOT + FACILITY;
        mockMvc.perform(post(url)
                .param("building", "building22")
                .param("address", "dexter동1")
                .param("openTime", "8:30")
                .param("closeTime", "15:30")
                .param("maxReserveCnt", "10")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(url))
                .andExpect(flash().attributeExists("message"));
    }

    // 시설 수정 폼 - 입력 에러
    @Test
    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 수정 폼 - 입력 값 에러")
    void updateFacility_input_error() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        String url = ROOT + FACILITY + "/" + facility.getId() + ROOT + SETTINGS + ROOT + FACILITY;
        mockMvc.perform(post(url)
                .param("building", "buildidwfwfwefwefweng22")
                .param("address", "dexter동1")
                .param("openTime", "8:30")
                .param("closeTime", "30:30")
                .param("maxReserveCnt", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("facility"))
                .andExpect(model().attributeExists("facilityForm"));
    }

    // 시설 삭제 폼
    @Test
    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 삭제 폼")
    void deleteFacilityForm() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        mockMvc.perform(get(ROOT + FACILITY + "/" + facility.getId() + ROOT + SETTINGS + DELETE))
                .andExpect(status().isOk())
                .andExpect(view().name(FACILITY + ROOT + SETTINGS + DELETE))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("facility"));
    }

    @Test
    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 삭제")
    void deleteFacility() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        Long id = facility.getId();
        mockMvc.perform(delete(ROOT + FACILITY + "/" + id + ROOT + SETTINGS + DELETE)
                        .with(csrf()))
                .andExpect(status().isOk());

        Optional<Facility> destroy = facilityRepository.findById(id);
        assertFalse(destroy.isPresent());
    }

    @Test
    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("시설 예약 - 성공")
    void reservationFacility() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        Long id = facility.getId();
        String date = LocalDate.now().toString();
        mockMvc.perform(get(ROOT + FACILITY + "/" + id + ROOT + SETTINGS + ROOT + RESERVATION + "?reservationDate=" + date))
                .andExpect(status().isOk());
    }
}
