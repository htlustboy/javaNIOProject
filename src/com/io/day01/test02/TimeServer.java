package com.io.day01.test02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * java 伪异步IO
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port:" + port);
            Socket socket = null;
            //创建IO任务线程池
            TimeServerHandlerExecutorPool singleExecutor = new TimeServerHandlerExecutorPool(50,10000);
            while (true) {
                socket = server.accept();
                singleExecutor.executor(new TimeServerHnadler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                System.out.println("The server closed");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }
    }
}
