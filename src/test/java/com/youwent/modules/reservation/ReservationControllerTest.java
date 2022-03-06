package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.WithAccount;
import com.youwent.modules.account.WithAdmin;
import com.youwent.infra.MockMvcTest;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.account.AccountRepository;
import com.youwent.modules.facility.FacilityFactory;
import com.youwent.modules.facility.FacilityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.youwent.modules.common.Url.*;

@MockMvcTest
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FacilityFactory facilityFactory;

    @Autowired
    private ReservationFactory reservationFactory;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @WithAccount(value = "dexter@gmail.com")
    @DisplayName("시설 예약 취소 - 성공")
    void reservation_cancel() throws Exception {
        Account account = accountRepository.findByEmail("admin@gmail.com");
        Facility facility = facilityFactory.createFacility(account);
        int nowCnt = facility.getNowReserveCnt();
        Reservation reservation = reservationFactory.createReservation(account,facility);
        Long id = reservation.getId();
        mockMvc.perform(put(ROOT + RESERVATION + ROOT + id)
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(reservation.isStatus());
        assertNotEquals(nowCnt, facility.getNowReserveCnt());
    }
}
