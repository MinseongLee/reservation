package com.youwent.modules.facility;

import com.youwent.modules.account.WithAdmin;
import com.youwent.infra.MockMvcTest;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.account.AccountRepository;
import com.youwent.modules.facility.FacilityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.youwent.modules.common.Url.*;

@MockMvcTest
public class FacilityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 등록 - 폼 조회")
    void createStudyForm() throws Exception {
        mockMvc.perform(get(ROOT + FACILITY + FORM))
                .andExpect(status().isOk())
                .andExpect(view().name(FACILITY + FORM))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("facilityForm"));
    }

    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 등록 - 입력값 정상")
    @Test
    void createFacility_correct_input() throws Exception {
        mockMvc.perform(post(ROOT + FACILITY + FORM)
                        .param("building", "덱스터")
                        .param("address", "서울시 덱스터동")
                        .param("openTime", "9:30")
                        .param("closeTime", "19:30")
                        .param("maxReserveCnt", "10")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + FACILITY));

        Facility facility = facilityRepository.findByBuilding("덱스터");
        assertNotNull(facility);
    }

    @WithAdmin(value = "admin@gmail.com")
    @DisplayName("시설 등록 - 입력값 에러")
    @Test
    void createFacility_error_input() throws Exception {
        mockMvc.perform(post(ROOT + FACILITY + FORM)
                .param("building", "dexter")
                .param("address", "서울시 덱스터")
                .param("openTime", "25:10")
                .param("closeTime", "12:70")
                .param("maxReserveCnt", "0")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(FACILITY + FORM));
    }
}
