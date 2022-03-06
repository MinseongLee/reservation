package com.youwent.modules.facility.dto;

import com.youwent.modules.facility.Facility;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacilityDto {
    private int nowReserveCnt;

    private boolean possibleReservation;

    public static FacilityDto createFacilityDto(Facility facility) {
        FacilityDto facilityDto = new FacilityDto();
        facilityDto.setNowReserveCnt(facility.getNowReserveCnt());
        facilityDto.setPossibleReservation(facility.isPossibleReservation());
        return facilityDto;
    }

}
