package com.sparta.billy.service;

import com.sparta.billy.dto.ReservationDto.ReservationCountDto;
import com.sparta.billy.dto.ReservationDto.ReservationDetailResponseDto;
import com.sparta.billy.dto.ReservationDto.ReservationRequestDto;
import com.sparta.billy.dto.ReservationDto.ReservationStateRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.model.*;
import com.sparta.billy.repository.ReservationDateRepository;
import com.sparta.billy.repository.ReservationQueryRepository;
import com.sparta.billy.repository.ReservationRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationQueryRepository reservationQueryRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final Check check;

    @Transactional
    public ResponseDto<?> createReservation(ReservationRequestDto reservationRequestDto,
                                            HttpServletRequest request) throws ParseException {
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
                .reviewCheck(false)
                .build();
        reservationRepository.save(reservation);

        final String DATE_PATTERN = "yyyy/MM/dd";
        String inputStartDate = reservationRequestDto.getStartDate();
        String inputEndDate = reservationRequestDto.getEndDate();;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        Date startDate = sdf.parse(inputStartDate);
        Date endDate = sdf.parse(inputEndDate);

        List<String> dates = new ArrayList<>();
        Date currentDate = startDate;
        while (currentDate.compareTo(endDate) <= 0) {
            dates.add(sdf.format(currentDate));
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DAY_OF_MONTH, 1);
            currentDate = c.getTime();
        }

        for (String date : dates) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .reservationDate(date)
                    .reservation(reservation)
                    .build();
            reservationDateRepository.save(reservationDate);
        }
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

        if (reservationStateRequestDto.getState() == 3) {
            reservationDateRepository.deleteAllByReservation(reservation);
        }
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
        reservationDateRepository.deleteAllByReservation(reservation);
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

    @Transactional
    public ResponseDto<?> getReservationByJullyAndState(int state, HttpServletRequest request) {
        Member jully = check.validateMember(request);
        check.tokenCheck(request, jully);
        List<ReservationDetailResponseDto> response = reservationQueryRepository.findReservationByJullyAndState(jully, state);
        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<?> getReservationCountByBillyAndState(HttpServletRequest request) {
        Member billy = check.validateMember(request);
        check.tokenCheck(request, billy);

        int state1 = reservationRepository.countReservationByBillyAndState(billy, 1);
        int state2 = reservationRepository.countReservationByBillyAndState(billy, 2);
        int state3 = reservationRepository.countReservationByBillyAndState(billy, 3);
        int state4 = reservationRepository.countReservationByBillyAndState(billy, 4);
        int state5 = reservationRepository.countReservationByBillyAndState(billy, 5);

        ReservationCountDto reservationCountDto = new ReservationCountDto(state1, state2, state3, state4, state5);

        return ResponseDto.success(reservationCountDto);
    }

    @Transactional
    public ResponseDto<?> getReservationCountByJullyAndState(HttpServletRequest request) {
        Member jully = check.validateMember(request);
        check.tokenCheck(request, jully);

        int state1 = reservationRepository.countReservationByJullyAndState(jully, 1);
        int state2 = reservationRepository.countReservationByJullyAndState(jully, 2);
        int state3 = reservationRepository.countReservationByJullyAndState(jully, 3);
        int state4 = reservationRepository.countReservationByJullyAndState(jully, 4);
        int state5 = reservationRepository.countReservationByJullyAndState(jully, 5);

        ReservationCountDto reservationCountDto = new ReservationCountDto(state1, state2, state3, state4, state5);

        return ResponseDto.success(reservationCountDto);
    }
}
