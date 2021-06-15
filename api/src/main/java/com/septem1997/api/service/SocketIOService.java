package com.septem1997.api.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.septem1997.api.config.JwtConfig;
import com.septem1997.api.entity.Barrage;
import com.septem1997.api.entity.User;
import com.septem1997.api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService {
    
    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    private static final String SEND_MSG = "sendMsg";
    private static final String RECEIVE_MSG = "receiveMsg";


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private BarrageService barrageService;
    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            log.info(" 用户： " + getUserByClient(client) + " 已连接 ");
//            client.sendEvent("connected", "你成功连接上了哦...");
//            String userId = getParamsByClient(client);
//            if (userId != null) {
//                clientMap.put(userId, client);
//            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            log.info(" 用户： " + getUserByClient(client) + " 已断开连接 ");
//            String userId = getParamsByClient(client);
//            if (userId != null) {
//                clientMap.remove(userId);
//                client.disconnect();
//            }
        });

        // 自定义事件 -> 监听客户端消息
        socketIOServer.addEventListener(SEND_MSG, Barrage.class, (client, data, ackSender) -> {
            User user = getUserByClient(client);
            log.info("  收到来自："+ user +"的消息:" + data.getContent());

            Barrage barrage = barrageService.save(Barrage.builder()
                    .content(data.getContent())
                    .user(user)
                    .build());

            // 转发消息
            socketIOServer.getBroadcastOperations().sendEvent(RECEIVE_MSG, barrage);
        });

        // 启动服务
        socketIOServer.start();
        log.info("启动socket.io服务");
    }

    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 获取连接的客户端ip地址
     */
    private User getUserByClient(SocketIOClient client) {
        String token = client.getHandshakeData().getUrlParams().get("token").get(0);
        final Claims tokenClaim = jwtConfig.getTokenClaim(token);
        return userRepository.findUserByUsernameAndIsDeleteFalse(tokenClaim.getSubject());
    }

}
