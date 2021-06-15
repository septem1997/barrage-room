package com.septem1997.api.config;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.septem1997.api.entity.Barrage;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIO {
    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
//        config.setHostname("localhost");
        config.setPort(8081);
        config.setOrigin(":*:");
        return new SocketIOServer(config);
    }
}
