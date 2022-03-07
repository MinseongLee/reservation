package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.CurrentAccount;
import com.youwent.modules.common.CustomResponse;
import com.youwent.modules.reservation.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.youwent.modules.common.Url.*;

@Controller
@RequestMapping(ROOT + RESERVATION)
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // account의 예약 리스트
    @GetMapping
    public String reservations(@CurrentAccount Account account, Model model,
                               String keyword, String orderByBuilding,
                               @PageableDefault(size = 10) Pageable pageable) {
        keyword = reservationService.getKeyword(keyword);
        orderByBuilding = reservationService.getOrderByBuilding(orderByBuilding);

        Page<ReservationDto> reservations = reservationService.getReservationsByKeyword(account, keyword, orderByBuilding, pageable);

        model.addAttribute(account);
        model.addAttribute("reservations", reservations);
        model.addAttribute("keyword", keyword);
        model.addAttribute("orderByBuilding", orderByBuilding);
        return RESERVATION + ROOT + INDEX;
    }

    @PutMapping(ID)
    @ResponseBody
    public CustomResponse UpdateReservationStatus(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservation(id);
        reservationService.updateStatus(reservation);
        return CustomResponse.defaultCustomResponse();
    }
}
