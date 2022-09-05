package com.sparta.billy.controller;

import com.sparta.billy.dto.ReservationDto.ReservationRequestDto;
import com.sparta.billy.dto.ReservationDto.ReservationStateRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/auth/reservations")
    public ResponseDto<?> reservationCreate(@RequestBody ReservationRequestDto reservationRequestDto,
                                            HttpServletRequest request) {
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
    public ResponseDto<?> deliveryCompleted(@PathVariable Long reservationId,
                                            HttpServletRequest request) {
        return reservationService.completedDelivery(reservationId, request);
    }
}
