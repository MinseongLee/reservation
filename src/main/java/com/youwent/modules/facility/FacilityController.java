package com.youwent.modules.facility;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.CurrentAccount;
import com.youwent.modules.facility.dto.FacilityForm;
import com.youwent.modules.facility.validator.FacilityFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.youwent.modules.common.Url.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT + FACILITY)
public class FacilityController {
    // 시설 관리에서 모든 시설들을 보여주고, 거기에서 시설 추가 버튼이 있어야한다.

    private final FacilityService facilityService;
    private final FacilityRepository facilityRepository;
    private final FacilityFormValidator facilityFormValidator;

    @InitBinder("facilityForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(facilityFormValidator);
    }

    @GetMapping
    public String facilities(@CurrentAccount Account account, Model model) {
        // 여기서 존재하는 모든 facility를 보여줄 것. paging 처리해서
        model.addAttribute(account);
        // facilities를 model로 넘겨줄 것.
        // 예약자는 제외하고 넘길 것.
        List<Facility> facilities = facilityService.getFacilities(account);
        model.addAttribute("facilities", facilities);
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

    @GetMapping(SEARCH)
    public String searchFacilities(@CurrentAccount Account account, String keyword, String orderByBuilding, Model model) {
        // asc or desc
        List<Facility> facilities = facilityService.getFacilitiesByKeyword(account, keyword, orderByBuilding);
        // 현재 예약을 했다면, 예약한 user는 제외
        model.addAttribute("facilities", facilities);
        model.addAttribute(account);
        return FACILITY + ROOT + INDEX;
    }
}
