package com.io.day01.test03;

/**
 * java NIO
 */
public class NIOTimeServer {

    public static void main(String[] args) {
        int port = 8080;
        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer,"NIO-TimeServer-001").start();
    }
}
