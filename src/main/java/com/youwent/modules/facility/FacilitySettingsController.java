package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.CurrentAccount;
import com.youwent.modules.common.CustomResponse;
import com.youwent.modules.facility.dto.FacilityDto;
import com.youwent.modules.facility.dto.FacilityForm;
import com.youwent.modules.facility.validator.FacilityFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.youwent.modules.common.Url.*;

@Controller
@RequestMapping(ROOT + FACILITY + ID + ROOT + SETTINGS)
@RequiredArgsConstructor
public class FacilitySettingsController {

    private final FacilityService facilityService;
    private final ModelMapper modelMapper;
    private final FacilityFormValidator facilityFormValidator;

    @InitBinder("facilityForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(facilityFormValidator);
    }

    @GetMapping(ROOT + FACILITY)
    public String updateFacilityForm(@CurrentAccount Account account, @PathVariable Long id, Model model) {
        Facility facility = facilityService.getFacility(id);
        model.addAttribute(account);
        model.addAttribute(facility);
        model.addAttribute(modelMapper.map(facility, FacilityForm.class));
        return FACILITY + ROOT + SETTINGS + ROOT + FACILITY;
    }

    @PostMapping(ROOT + FACILITY)
    public String updateFacility(@CurrentAccount Account account, @PathVariable Long id,
                                 @Valid FacilityForm facilityForm, Errors errors, Model model, RedirectAttributes attributes) {
        Facility facility = facilityService.getFacility(id);
        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(facility);
            return FACILITY + ROOT + SETTINGS + ROOT + FACILITY;
        }
        facilityService.updateFacility(facility, facilityForm);
        attributes.addFlashAttribute("message", "시설 정보를 수정하였습니다.");
        return REDIRECT + ROOT + FACILITY + ID + ROOT + SETTINGS + ROOT + FACILITY;
    }

    @GetMapping(DELETE)
    public String deleteFacilityForm(@CurrentAccount Account account, @PathVariable Long id, Model model) {
        Facility facility = facilityService.getFacility(id);
        model.addAttribute(account);
        model.addAttribute(facility);
        return FACILITY + ROOT + SETTINGS + DELETE;
    }

    @DeleteMapping(DELETE)
    @ResponseBody
    public CustomResponse deleteFacility(@PathVariable Long id) {
        Facility facility = facilityService.getFacility(id);
        facilityService.deleteFacility(facility);
        return CustomResponse.defaultCustomResponse();
    }

    @GetMapping(ROOT + RESERVATION)
    @ResponseBody
    public CustomResponse userReservation(@CurrentAccount Account account, @PathVariable Long id,
                                          @RequestParam("reservationDate") String reservationDate) {
        // date format yyyy-mm-dd
        Facility facility = facilityService.reservationFacility(id, account, reservationDate);
        return CustomResponse.responseWithFacilityDto(FacilityDto.createFacilityDto(facility));
    }
}
