package com.sparta.billy.exception;

import com.sparta.billy.exception.ex.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATE_EMAIL.getCode(), ErrorCode.DUPLICATE_EMAIL.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.MEMBER_NOT_FOUND.getCode(), ErrorCode.MEMBER_NOT_FOUND.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfileNotFoundException(){
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.PROFILE_NOT_FOUND.getCode(), ErrorCode.PROFILE_NOT_FOUND.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundPostException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND_POST.getCode(), ErrorCode.NOT_FOUND_POST.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeletePostException.class)
    public ResponseEntity<ErrorResponse> handleDeletePostException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.ALREADY_DELETE_POST.getCode(), ErrorCode.ALREADY_DELETE_POST.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundReviewException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundReviewException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND_REVIEW.getCode(), ErrorCode.NOT_FOUND_REVIEW.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundReservationException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND_RESERVATION.getCode(), ErrorCode.NOT_FOUND_RESERVATION.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeliveryNotYetException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryNotYetException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DELIVERY_NOT_YET.getCode(), ErrorCode.DELIVERY_NOT_YET.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_AUTHOR.getCode(), ErrorCode.NOT_AUTHOR.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.TOKEN_EXPIRED.getCode(), ErrorCode.TOKEN_EXPIRED.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotExistException.class)
    public ResponseEntity<ErrorResponse> handleTokenNotExistException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.TOKEN_NOT_EXIST.getCode(), ErrorCode.TOKEN_NOT_EXIST.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundChatRoomException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundChatRoomException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND_CHATROOM.getCode(), ErrorCode.NOT_FOUND_CHATROOM.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        Exception exception = new Exception();
        exception.setCode(HttpStatus.BAD_REQUEST);
        exception.setMessage(ex.getMessage());

        return new ResponseEntity(
                exception,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleApiRequestException(NullPointerException ex) {
        Exception exception = new Exception();
        exception.setCode(HttpStatus.BAD_REQUEST);
        exception.setMessage(ex.getMessage());

        return new ResponseEntity(
                exception,
                HttpStatus.BAD_REQUEST
        );
    }
}

