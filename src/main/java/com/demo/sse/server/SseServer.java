package com.demo.sse.server;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseServer {

    public SseEmitter conect(String userId);
    
    public boolean send(String userId, String content);

    public boolean close(String userId);
    
}
