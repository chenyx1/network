package com.chenyx.socketchannel.multiplex;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ：chenyx
 * @description：多路复用事件处理
 * @date ：2021/6/30 10:15
 */
public abstract class BaseHandEvent implements IHandleEvent{

    @Override
    public void connectEvent(SocketChannel socketChannel) throws IOException {
        System.out.println(socketChannel.getRemoteAddress() + "已连接！");
    }

    @Override
    public void acceptEvent(ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel != null) {
            System.out.println(socketChannel.getRemoteAddress() + "请求已接收到！");
        }
    }

    @Override
    public void readEvent(SocketChannel socketChannel) throws IOException {
        System.out.println(socketChannel.getRemoteAddress() + "消息读取！");
    }

    @Override
    public void writeEvent(SocketChannel socketChannel) throws IOException {
        System.out.println(socketChannel.getRemoteAddress() + "消息发送！");
    }
}
