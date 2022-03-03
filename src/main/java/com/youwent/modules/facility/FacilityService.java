package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import com.youwent.modules.facility.dto.FacilityForm;
import com.youwent.modules.main.GlobalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
//    private final AccountFacilityRepository accountFacilityRepository;
    private final GlobalService globalService;
    private final ModelMapper modelMapper;

    public Facility createNewFacility(FacilityForm facilityForm, Account account) {
        LocalTime open = globalService.stringToLocalTime(facilityForm.getOpenTime());
        LocalTime close = globalService.stringToLocalTime(facilityForm.getCloseTime());
        // 변경이 일어나려면 account를 영속상태로 만들고 넣어줘야해.. 그렇다면 차라리 즉시로딩이 좋지.
        Facility facility = Facility.builder()
                                    .building(facilityForm.getBuilding())
                                    .address(facilityForm.getAddress())
                                    .openTime(open)
                                    .closeTime(close)
                                    .maxReserveCnt(facilityForm.getMaxReserveCnt())
                                    .possibleReservation(true)
                                    .createdDate(LocalDateTime.now())
                                    .account(account)
                                    .build();

//        facility.addAccountFacility(account);
        Facility newFacility = facilityRepository.save(facility);
//        newFacility.addAccountFacility(account);
//        newFacility.addAdmin(account);
        return newFacility;
    }
}
