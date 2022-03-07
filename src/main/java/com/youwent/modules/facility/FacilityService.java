package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import com.youwent.modules.facility.dto.FacilityForm;
import com.youwent.modules.main.GlobalService;
import com.youwent.modules.reservation.Reservation;
import com.youwent.modules.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final GlobalService globalService;
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;

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
            throw new IllegalArgumentException("해당하는 시설이 존재하지 않습니다.");
        }
        return facility.get();
    }

    // repository에서 가져온 객체(facility)이므로 영속상태이다. 그러므로 save() 불필요
    public void updateFacility(Facility facility, FacilityForm facilityForm) {
        modelMapper.map(facilityForm, facility);
    }

    public void deleteFacility(Facility facility) {
        // facility와 연관된 reservation도 같이 삭제
        reservationRepository.deleteByFacility(facility);
        // mapper table에 있는 facility도 같이 삭제된다.
        facilityRepository.delete(facility);
    }

    //예약된 적이 있는지 확인 후 없으면 생성
    public Facility reservationFacility(Long id, Account account, String reservationDate) {
        Facility facility = getFacility(id);
        // 날짜 밸리데이션 체크
        LocalDateTime reservedDate = getReservationDate(reservationDate);
        // reservedDate 기준으로 now < max 를 확인해야한다.
        Long reservationCounts = reservationRepository.findByReservedDate(facility, reservedDate);
        int nowReserveCnt = reservationCounts == null ? 0 : Math.toIntExact(reservationCounts);
        if (nowReserveCnt < facility.getMaxReserveCnt()) {
            // 전에 예약했던것이 존재하는지 확인.
            Reservation reservation = reservationRepository.findFirstByAccount_IdAndFacility_Id(account.getId(), facility.getId());
            // 존재하지 않으면 생성
            if (reservation == null) {
                Reservation newReservation = Reservation.builder()
                        .reservedDate(reservedDate)
                        .status(true)
                        .createdDate(LocalDateTime.now())
                        .account(account)
                        .facility(facility)
                        .build();
                reservationRepository.save(newReservation);
            } else if (reservation.isStatus()){
                // 예약 한 상태라면 에러
                throw new IllegalArgumentException("현재 예약 상태입니다. 취소 후 다시 예약해주세요.");
            } else {
                // 예약 취소상태라면 예약
                reservation.reserve(reservedDate);
            }
            // 오늘인 경우
            // ++ and now==max : possibleReservation=false
            if (reservedDate.equals(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0)))) facility.plusNowReserveCnt();
        } else {
            throw new IllegalArgumentException("예약이 다 찼습니다.");
        }
        return facility;
    }

    // async for schedule
    // 자정에 오늘 예약자들 쭉 업데이트
    public void updateAllNowReserveCnt() {
        List<Facility> facilities = facilityRepository.findAll();
        for (Facility facility : facilities) {
            Long todayCnt = reservationRepository.findByReservationToday(facility);
            facility.setNowReserveCnt(todayCnt == null ? 0 : Math.toIntExact(todayCnt));
        }
    }

    // 날짜를 입력받는 메서드 날짜에 대한 벨리데이션 체크
    private LocalDateTime getReservationDate(String reservationDate) {
        try {
            if (globalService.isAfterToday(reservationDate)) {
                return globalService.stringToLocalDateTime(reservationDate);
            } else {
                throw new IllegalArgumentException("오늘보다 더 큰 숫자를 입력해주세요.");
            }
        } catch (NumberFormatException e) {
            log.error("숫자가 아닌 다른 심볼이 포함된 에러, ", e);
            throw new NumberFormatException("숫자만 입력해주세요.");
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            throw new DateTimeParseException("년 월 일 날짜를 yyyy-mm-dd에 맞게 정확하게 확인해주세요.", e.getParsedString(), e.getErrorIndex());
        }
    }


    public Page<Facility> getFacilitiesByKeyword(Account account, String keyword, String orderByBuilding, Pageable pageable) {
        // default desc
        return orderByBuilding.equals("asc") ? facilityRepository.findByKeywordOrderByAsc(account, keyword, pageable) : facilityRepository.findByKeywordOrderByDesc(account, keyword, pageable);
    }

    public String getKeyword(String keyword) {
        return keyword == null ? "" : keyword;
    }

    public String getOrderByBuilding(String orderByBuilding) {
        return orderByBuilding == null ? "" : orderByBuilding;
    }
}
