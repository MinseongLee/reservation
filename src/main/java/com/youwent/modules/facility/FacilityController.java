package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.CurrentAccount;
import com.youwent.modules.facility.dto.FacilityForm;
import com.youwent.modules.facility.validator.FacilityFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.youwent.modules.common.Url.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT + FACILITY)
public class FacilityController {
    // 시설 관리에서 모든 시설들을 보여주고, 거기에서 시설 추가 버튼이 있어야한다.

    private final FacilityService facilityService;
    private final FacilityFormValidator facilityFormValidator;

    @InitBinder("facilityForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(facilityFormValidator);
    }

    // all facilities
    @GetMapping
    public String facilities(@CurrentAccount Account account, Model model,
                             String keyword, String orderByBuilding,
                             @PageableDefault(size = 10) Pageable pageable) {
        keyword = facilityService.getKeyword(keyword);
        orderByBuilding = facilityService.getOrderByBuilding(orderByBuilding);
        // pageable : size, page, sort
        // 예약자는 제외하고 넘길 것.
        Page<Facility> facilities = facilityService.getFacilitiesByKeyword(account, keyword, orderByBuilding, pageable);
        model.addAttribute("facilities", facilities);
        model.addAttribute(account);
        model.addAttribute("keyword", keyword);
        model.addAttribute("orderByBuilding", orderByBuilding);
        return FACILITY + ROOT + INDEX;
    }

    // form 처리
    @GetMapping(FORM)
    public String facilityForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new FacilityForm());
        return FACILITY + FORM;
    }

    // 실제 create 처리.
    @PostMapping(FORM)
    public String createFacility(@CurrentAccount Account account, @Valid FacilityForm facilityForm, Errors errors) {
        if (errors.hasErrors()) {
            return FACILITY + FORM;
        }
        // 여기에서.. time에 대한 exception은? - 이걸 validator로 검사할 것.
        facilityService.createNewFacility(facilityForm, account);
        return REDIRECT + ROOT + FACILITY;
    }

    @GetMapping(ID)
    public String detailsFacility(@CurrentAccount Account account, @PathVariable Long id, Model model) {
        Facility facility = facilityService.getFacility(id);
        model.addAttribute(account);
        model.addAttribute(facility);
        return FACILITY + VIEW;
    }
}
