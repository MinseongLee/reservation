package com.youwent.modules.reservation;

import com.youwent.modules.account.Account;
import com.youwent.modules.account.CurrentAccount;
import com.youwent.modules.common.CustomResponse;
import com.youwent.modules.reservation.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.youwent.modules.common.Url.*;

@Controller
@RequestMapping(ROOT + RESERVATION)
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public String reservations(@CurrentAccount Account account, Model model) {
        List<Reservation> reservations = reservationService.getReservationsByAccount(account);
        List<ReservationDto> reservationDtoList = reservationService.getReservationDtoList(reservations);
        model.addAttribute(account);
        model.addAttribute("reservations", reservationDtoList);
        return RESERVATION + ROOT + INDEX;
    }

    @PutMapping(ID)
    @ResponseBody
    public CustomResponse UpdateReservationStatus(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservation(id);
        reservationService.updateStatus(reservation);
        return CustomResponse.defaultCustomResponse();
    }

    @GetMapping(SEARCH)
    public String searchReservations(@CurrentAccount Account account, String keyword, String orderByBuilding, Model model) {
        List<Reservation> reservations = reservationService.getReservationsByKeyword(account, keyword, orderByBuilding);
        List<ReservationDto> reservationDtoList = reservationService.getReservationDtoList(reservations);
        model.addAttribute(account);
        model.addAttribute("reservations", reservationDtoList);
        return RESERVATION + ROOT + INDEX;
    }
}
