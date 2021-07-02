package com.chenyx.socketchannel.multiplex;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author ：chenyx
 * @description 多路复用客户端端消息处理
 * @date ：2021/6/30 9:39
 */
public class SocketChannnelHandleEvent extends BaseHandEvent {


    @Override
    public void readEvent(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);//缓存容量为1024
        StringBuilder msgBuilder = new StringBuilder();
        while (socketChannel.read(buffer)  > 0) {
            buffer.flip();
            msgBuilder.append(new String(buffer.array()));
            buffer.clear();
        }
        System.out.println("client received:" + socketChannel.getRemoteAddress() + " message,content:" + msgBuilder.toString());
//        //重置
//        buffer.rewind();
//        buffer.put("The request has been accepted".getBytes());
//        buffer.flip();
//        socketChannel.write(buffer);
    }
}
