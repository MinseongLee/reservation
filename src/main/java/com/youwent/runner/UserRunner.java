package com.youwent.runner;


import com.youwent.modules.account.Account;
import com.youwent.modules.account.AccountRepository;
import com.youwent.modules.account.AccountService;
import com.youwent.modules.account.UserType;
import com.youwent.modules.account.dto.AccountDto;
import com.youwent.modules.facility.Facility;
import com.youwent.modules.facility.FacilityRepository;
import com.youwent.modules.facility.FacilityService;
import com.youwent.modules.facility.dto.FacilityForm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Profile({"local", "dev"})
@Transactional
@RequiredArgsConstructor
public class UserRunner implements ApplicationRunner {
    private final AccountService accountService;
    private final FacilityService facilityService;
    private final AccountRepository accountRepository;
    private final FacilityRepository facilityRepository;

    // test data
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 어드민 유저
        AccountDto admin = new AccountDto();
        admin.setEmail("dexlee@dexter.com");
        admin.setPassword("dexter1234");
        admin.setName("dexter");
        admin.setPhone("01022223333");
        Account adminAccount = accountService.processNewAccount(admin);
        adminAccount.completeSignup();
        Account byEmail = accountRepository.findByEmail("dexlee@dexter.com");
        byEmail.setUserType(UserType.ADMIN);
        accountService.login(adminAccount);

        // 시설 생성
        for (int i = 0; i < 31; i++) {
            FacilityForm facilityForm = new FacilityForm();
            facilityForm.setBuilding("dexterBuilding"+i);
            facilityForm.setAddress("dexterAddress"+i);
            facilityForm.setOpenTime("9:10");
            facilityForm.setCloseTime("22:40");
            facilityForm.setMaxReserveCnt(4+i);
            facilityService.createNewFacility(facilityForm, adminAccount);
        }
        // 일반 유저 생성
        AccountDto user = new AccountDto();
        user.setEmail("dexter@dexter.com");
        user.setPassword("dexter1234");
        user.setName("dexUser");
        user.setPhone("01033335555");
        Account account = accountService.processNewAccount(user);
        account.completeSignup();
        accountService.login(account);


        // 시설 예약
        List<Facility> facilityList = facilityRepository.findAll();
        for (Facility facility : facilityList) {
            if (facility.getId() % 2 == 0) {
                facilityService.reservationFacility(facility.getId(), account, LocalDate.now().toString());
            }
        }
    }
}
