package com.chatapp.client.workers;

import com.chatapp.client.SocketClient;
import com.chatapp.commons.response.AuthResponse;
import com.chatapp.commons.response.Response;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Worker;
import lombok.NonNull;
import lombok.Synchronized;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.StreamCorruptedException;

public class AuthSocketService extends SocketService {
    private static volatile AuthSocketService INSTANCE;

    @Synchronized
    public static AuthSocketService getInstance(@NonNull SocketClient socketClient) {
        if (INSTANCE == null) {
            INSTANCE = new AuthSocketService(socketClient);
        }
        if (!INSTANCE.isRunning())
            INSTANCE.start();
        return INSTANCE;
    }

    @Synchronized
    public static AuthSocketService getInstance() {
        if (INSTANCE == null) throw new NullPointerException("User Socket Service was not defined");
        return INSTANCE;
    }

    private AuthSocketService(@NonNull SocketClient socketClient) {
        super(socketClient);
    }

    @Override
    protected void listenResponse() throws Exception {
        while (isAlive.get()) {
            try {
                if (!isAlive.get()) return;
                System.out.println("Wait res");
                Object input = socketClient.getResponse();
                System.out.println(input);
                if (ObjectUtils.isEmpty(input)) {
                    continue;
                }
                resQueue.put((Response) input);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new Exception("ListenError");
            } catch (Exception err) {
                System.out.println("ERROR");
//            err.printStackTrace();
            }
        }

    }
}
