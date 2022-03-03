package com.youwent.modules.facility.validator;

import com.youwent.modules.facility.dto.FacilityForm;
import com.youwent.modules.main.GlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class FacilityFormValidator implements Validator {

    private final GlobalService globalService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FacilityForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FacilityForm facilityForm = (FacilityForm) target;
        String open = facilityForm.getOpenTime();
        String close = facilityForm.getCloseTime();

        // 시간이 유효하지 않다면,
        if (!globalService.isLocalTime(open)) {
            errors.rejectValue("openTime", "invalid.value", "오픈시간을 0~23시, 0~59분 안으로 입력해주세요.");
        }
        if (!globalService.isLocalTime(close)) {
            errors.rejectValue("closeTime", "invalid.value", "클로즈시간을 0~23시, 0~59분 안으로 입력해주세요.");
        }

        LocalTime openTime = globalService.stringToLocalTime(open);
        LocalTime closeTime = globalService.stringToLocalTime(close);

        // 오픈시간이 클로즈시간보다 큰 경우
        if(openTime.isAfter(closeTime)) {
            errors.rejectValue("closeTime", "wrong.value", "오픈시간보다 클로즈시간을 더 크게 적어주세요.");
        }
    }
}
