package www.com.springboot_iv.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exceptionHandler(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		ex.printStackTrace();
		return new ResponseEntity<>("{ \"message\": \"API is error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return new ResponseEntity<>(
				"{ \"message\": \"" + e.getBindingResult().getAllErrors().stream()
						.map(error -> error.getDefaultMessage()).collect(Collectors.joining("\n")) + "\"}",
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
		return new ResponseEntity<>("{ \"message\": \"Id is not found\"}", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException e) {
		return new ResponseEntity<>("{ \"message\": \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
	}

}
