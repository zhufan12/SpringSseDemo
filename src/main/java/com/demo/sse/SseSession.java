package com.demo.sse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.demo.sse.exception.SseException;

public class SseSession {


    private static Map<String,SseEmitter> sessionMap = new ConcurrentHashMap<>();

    public static void add(String sessionKey,SseEmitter sseEmitter){
        if(sessionMap.get(sessionKey) != null){
           throw new SseException("User exists!");
        }
        sessionMap.put(sessionKey, sseEmitter);
    }
    
    public static boolean exists(String sessionKey){
        return sessionMap.get(sessionKey) != null;
    }

    public static boolean remove(String sessionKey){
        SseEmitter sseEmitter = sessionMap.get(sessionKey);
        if(sseEmitter != null){
            sseEmitter.complete();
            return true;
        }
        return false;
    }

    public static void onError(String sessionKey,Throwable throwable){
        SseEmitter sseEmitter = sessionMap.get(sessionKey);
        if(sseEmitter != null){
            sseEmitter.completeWithError(throwable);
        }
    }

    public static void send(String sessionKey,String content) throws IOException{
        sessionMap.get(sessionKey).send(content);
    }

}
