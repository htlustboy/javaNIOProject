package com.io.day01.test03;

public class NIOTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new TimeClientHandle("127.0.0.1",port),"NIO-TimeClient-001").start();
    }
}
