package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class FacilityFactory {
    @Autowired
    private final FacilityRepository facilityRepository;

    public Facility createFacility(Account account) {
        Facility facility = Facility.builder()
                                    .building("dexter building")
                                    .address("dexter동")
                                    .openTime(LocalTime.of(9, 30))
                                    .closeTime(LocalTime.of(15, 20))
                                    .maxReserveCnt(20)
                                    .possibleReservation(true)
                                    .createdDate(LocalDateTime.now())
                                    .account(account)
                                    .build();
        facilityRepository.save(facility);
        return facility;
    }
}
