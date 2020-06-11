package com.io.day02.netty01;/**
 * Created by taohu on 2020/6/11.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author hutao
 * @prpgram javaNIOProject
 * @description
 * @create 2020/06/11 14:58:35
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMsg;

    public TimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMsg = Unpooled.buffer(req.length);
        firstMsg.writeBytes(req);
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMsg);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable e){
        ctx.close();
    }
}
