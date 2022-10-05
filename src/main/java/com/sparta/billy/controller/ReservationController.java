package com.sparta.billy.controller;

import com.sparta.billy.dto.ReservationDto.ReservationCountDto;
import com.sparta.billy.dto.ReservationDto.ReservationDetailResponseDto;
import com.sparta.billy.dto.ReservationDto.ReservationRequestDto;
import com.sparta.billy.dto.ReservationDto.ReservationStateRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/auth/reservations")
    public ResponseDto<String> reservationCreate(@RequestBody ReservationRequestDto reservationRequestDto,
                                            HttpServletRequest request) throws ParseException {
        return reservationService.createReservation(reservationRequestDto, request);
    }

    @PatchMapping("/auth/reservations/jully/{reservationId}")
    public ResponseEntity<SuccessDto> changeStateByJully(@PathVariable Long reservationId,
                                                         @RequestBody ReservationStateRequestDto reservationStateRequestDto,
                                                         HttpServletRequest request) {
        return reservationService.changeStateByJully(reservationId, reservationStateRequestDto, request);
    }

    @PatchMapping("/auth/reservations/billy/{reservationId}")
    public ResponseEntity<SuccessDto> cancelByBilly(@PathVariable Long reservationId,
                                                 @RequestBody ReservationStateRequestDto reservationStateRequestDto,
                                                 HttpServletRequest request) {
        return reservationService.cancelReservationByBilly(reservationId, reservationStateRequestDto, request);
    }

    @PatchMapping("/auth/reservations/billy/delivery/{reservationId}")
    public ResponseDto<String> deliveryCompleted(@PathVariable Long reservationId,
                                            HttpServletRequest request) {
        return reservationService.completedDelivery(reservationId, request);
    }

    @GetMapping("/auth/reservations/billy/{state}")
    public ResponseDto<List<ReservationDetailResponseDto>> reservationListByBilly(@PathVariable int state,
                                                                                  HttpServletRequest request) {
        return reservationService.getReservationByBillyAndState(state, request);
    }

    @GetMapping("/auth/reservations/jully/{state}")
    public ResponseDto<List<ReservationDetailResponseDto>> reservationListByJully(@PathVariable int state,
                                                 HttpServletRequest request) {
        return reservationService.getReservationByJullyAndState(state, request);
    }

    @GetMapping("/auth/reservations/billy")
    public ResponseDto<ReservationCountDto> reservationCountByBilly(HttpServletRequest request) {
        return reservationService.getReservationCountByBillyAndState(request);
    }

    @GetMapping("/auth/reservations/jully")
    public ResponseDto<ReservationCountDto> reservationCountByJully(HttpServletRequest request) {
        return reservationService.getReservationCountByJullyAndState(request);
    }
}
