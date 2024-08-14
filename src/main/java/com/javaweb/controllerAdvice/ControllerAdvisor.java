package com.javaweb.controllerAdvice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.javaweb.Model.ErrorResponseDTO;

import customexception.FieldRequiredException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
//	lỗi toán học 
	 @ExceptionHandler(ArithmeticException.class)
	    public ResponseEntity<Object> handleArithmeticException(
	            ArithmeticException ex, WebRequest request) {

	       ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
	       errorResponseDTO.setError(ex.getMessage());
	       List<String> details = new ArrayList<>();
	       details.add("số nguyên ko chia đc cho 0");
	       errorResponseDTO.setDetail(details);

	        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

//	 lỗi nhập thiết field
	 @ExceptionHandler(FieldRequiredException.class)
	    public ResponseEntity<Object> handleFieldRequiredException(
	            FieldRequiredException ex, WebRequest request) {

	       ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
	       errorResponseDTO.setError(ex.getMessage());
	       List<String> details = new ArrayList<>();
	       details.add("check lại name hoặc numberOfBasement bởi vì giá trị đang null");
	       errorResponseDTO.setDetail(details);

	        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_GATEWAY);
	    }
	
}
