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
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
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

        Facility newFacility = facilityRepository.save(facility);
        return newFacility;
    }

    public Facility getFacility(Long id) {
        Optional<Facility> facility = facilityRepository.findById(id);
        if (!facility.isPresent()) {
            throw new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다.");
        }
        return facility.get();
    }

    // repository에서 가져온 객체(facility)이므로 영속상태이다. 그러므로 save() 불필요
    public void updateFacility(Facility facility, FacilityForm facilityForm) {
        modelMapper.map(facilityForm, facility);
    }

    public void deleteFacility(Facility facility) {
        facilityRepository.delete(facility);
    }
}
