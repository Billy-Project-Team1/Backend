package com.sparta.billy.service;

import com.sparta.billy.dto.ReservationDto.ReservationDetailResponseDto;
import com.sparta.billy.dto.ReservationDto.ReservationRequestDto;
import com.sparta.billy.dto.ReservationDto.ReservationStateRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Reservation;
import com.sparta.billy.repository.ReservationQueryRepository;
import com.sparta.billy.repository.ReservationRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationQueryRepository reservationQueryRepository;
    private final Check check;

    @Transactional
    public ResponseDto<?> createReservation(ReservationRequestDto reservationRequestDto,
                                            HttpServletRequest request) {
        Member billyMember = check.validateMember(request);
        Post post = check.getCurrentPost(reservationRequestDto.getPostId());
        check.checkPost(post);
        check.tokenCheck(request, billyMember);

        Reservation reservation = Reservation.builder()
                .post(post)
                .jully(post.getMember())
                .billy(billyMember)
                .state(1)
                .delivery(false)
                .startDate(reservationRequestDto.getStartDate())
                .endDate(reservationRequestDto.getEndDate())
                .build();
        reservationRepository.save(reservation);
        return ResponseDto.success("예약이 접수되었습니다.");
    }

    // 줄리의 예약 변경 (예약 거부 / 예약 승인)
    @Transactional
    public ResponseEntity<SuccessDto> changeStateByJully(Long reservationId,
                                             ReservationStateRequestDto reservationStateRequestDto,
                                             HttpServletRequest request) {
        Member jully = check.validateMember(request);
        Reservation reservation = check.getCurrentReservation(reservationId);
        check.checkReservation(reservation);
        check.checkReservationPostAuthor(jully, reservation);
        check.validateReservation(reservation);
        check.validateDelivery(reservationStateRequestDto.getState(), reservation);

        reservation.changeState(reservationStateRequestDto);
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    // 빌리의 예약 취소
    @Transactional
    public ResponseEntity<SuccessDto> cancelReservationByBilly(Long reservationId,
                                                   ReservationStateRequestDto reservationStateRequestDto,
                                                   HttpServletRequest request) {
        Member billy = check.validateMember(request);
        Reservation reservation = check.getCurrentReservation(reservationId);
        check.checkReservation(reservation);
        check.checkReservationAuthor(billy, reservation);
        check.validateReservation(reservation);

        if (reservation.getState() != 1) {
            throw new IllegalArgumentException("이미 진행중인 예약건입니다.");
        }
        if (reservationStateRequestDto.getState() != 3) {
            reservationStateRequestDto.setState(3);
        }

        reservation.changeState(reservationStateRequestDto);
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    // 빌리의 수령 완료 처리
    @Transactional
    public ResponseDto<?> completedDelivery(Long reservationId,
                                                    HttpServletRequest request) {
        Member billy = check.validateMember(request);
        Reservation reservation = check.getCurrentReservation(reservationId);
        check.checkReservation(reservation);
        check.checkReservationAuthor(billy, reservation);
        check.validateReservation(reservation);

        if (reservation.getState() != 2) {
            throw new IllegalArgumentException("예약 확정인 예약건이 아닙니다.");
        }
        reservation.setDelivery();
        return ResponseDto.success("수령 완료!");
    }

    @Transactional
    public ResponseDto<?> getReservationByBillyAndState(int state, HttpServletRequest request) {
        Member billy = check.validateMember(request);
        check.tokenCheck(request, billy);
        List<ReservationDetailResponseDto> response = reservationQueryRepository.findReservationByBillyAndState(billy, state);
        return ResponseDto.success(response);
    }
}
