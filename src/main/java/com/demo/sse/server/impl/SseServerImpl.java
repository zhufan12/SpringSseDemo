package com.demo.sse.server.impl;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.demo.sse.SseSession;
import com.demo.sse.exception.SseException;
import com.demo.sse.server.SseServer;

@Service
public class SseServerImpl implements SseServer  {

    private static final Logger log = LoggerFactory.getLogger(SseServerImpl.class);
    


    @Override
    public SseEmitter conect(String userId) {
        if(SseSession.exists(userId)){
            SseSession.remove(userId);
        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onError((err)-> {
            log.error("type: SseSession Error, msg: {} session Id : {}",err.getMessage(), userId);
            SseSession.onError(userId, err);
        });

        sseEmitter.onTimeout(() -> {
            log.info("type: SseSession Timeout, session Id : {}", userId);
            SseSession.remove(userId);
        });
        
        sseEmitter.onCompletion(() -> {
            log.info("type: SseSession Completion, session Id : {}", userId);
            SseSession.remove(userId);
        });
        SseSession.add(userId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public boolean send(String userId, String content) {
        if(SseSession.exists(userId)){
            try{
                SseSession.send(userId, content);
                return true;
            }catch(IOException exception){
                log.error("type: SseSession send Erorr:IOException, msg: {} session Id : {}",exception.getMessage(), userId);
            }
        }else{
            throw new SseException("User Id " + userId + " not Found");
        }
        return false;
    }

    @Override
    public boolean close(String userId) {
        log.info("type: SseSession Close, session Id : {}", userId);
        return SseSession.remove(userId);
    }
    
}
