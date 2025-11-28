//package com.shkvlnc.bujo_app.web;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
//        Map<String, String> error = new HashMap<>();
//        error.put("message", ex.getReason());
//        return ResponseEntity.status(ex.getStatusCode()).body(error);
//    }
//
//    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
//        String firstError = ex.getBindingResult().getFieldError() != null
//                ? ex.getBindingResult().getFieldError().getDefaultMessage()
//                : "Validation failed";
//        Map<String, String> error = new HashMap<>();
//        error.put("message", firstError);
//        return ResponseEntity.badRequest().body(error);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
//        Map<String, String> error = new HashMap<>();
//        error.put("message", "Invalid request payload: " + ex.getMostSpecificCause().getMessage());
//        return ResponseEntity.badRequest().body(error);
//    }
//}
