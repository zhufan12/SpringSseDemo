package com.demo.sse.handel;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.sse.exception.SseException;

@ControllerAdvice
@ResponseBody
public class SseExceptionHandel {
    

    @ExceptionHandler(value = SseException.class)
    public String handelSseException(SseException sseException){
        return "Error:" + sseException.getMessage();
    }
}
