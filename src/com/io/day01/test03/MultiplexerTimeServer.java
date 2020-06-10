package com.io.day01.test03;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    /**
     * 构造函数初始化资源
     * @param port
     */
    MultiplexerTimeServer(int port) {
        try {
            //创建多路复用选择器
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //设置为异步非阻塞
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("the time server is start in port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                //1秒被唤醒一次
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    handleInput(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            //处理新接入的请求
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel sc = serverSocketChannel.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            //读取客户端的请求
            if (selectionKey.isReadable()) {
                SocketChannel sc = (SocketChannel) selectionKey.channel();
                //设置一个1KB的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    //将缓冲区当前的limit设置成position，position=0，用于对后续缓冲区的读取操作
                    readBuffer.flip();
                    //根据缓冲区的可读取字节个数创建byte数组
                    byte[] bytes = new byte[readBuffer.remaining()];
                    //将缓冲区的可读取字节复制到新建的数组中
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("the time server receiver request:" + body);
                    //如果客户端发送的请求指令是query time request，则吧服务器当前时间编码后返回给客户端
                    String currentTime = "query time request".equalsIgnoreCase(body)
                            ? new Date(System.currentTimeMillis()).toString() : "error request";
                    doWrite(sc,currentTime);
                }else if(readBytes<0){
                    selectionKey.cancel();
                    sc.close();
                }else {
                    //读到0，忽略
                }
            }
        }
    }

    /**
     * 将应答消息异步返回给客户端
     * @param sc
     * @param currentTime
     * @throws IOException
     */
    private void doWrite(SocketChannel sc, String currentTime) throws IOException {
        if(currentTime!=null && currentTime.length()>0){
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(currentTime.length());
            //将字节数组复制到缓冲区
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
    }
}
