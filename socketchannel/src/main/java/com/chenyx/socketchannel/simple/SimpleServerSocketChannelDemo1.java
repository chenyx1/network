package com.chenyx.socketchannel.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ：chenyx
 * @description：ServerSocketChannel实现类
 * @date ：2021/6/23 14:37
 */
public class SimpleServerSocketChannelDemo1 {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress(9011);
        serverSocketChannel.bind(socketAddress);
        //采用非阻塞网络请求
        serverSocketChannel.configureBlocking(false);
        while (true) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel == null) {
                    continue;
                }
                //采用非阻塞网络请求
                serverSocketChannel.configureBlocking(false);
                ByteBuffer dst = ByteBuffer.allocate(512);
                socketChannel.read(dst);
                dst.flip();//切换模式
                String msg = new String(dst.array());
                System.out.println("SocketChannel:" + socketChannel.getRemoteAddress() + ",msg:" + msg);

                ByteBuffer buffer = ByteBuffer.allocate(512);
                String sMsg = "welcome " + socketChannel.getRemoteAddress().toString() + "!";
                buffer.put(sMsg.getBytes());
                buffer.flip();//切换模式
                socketChannel.write(buffer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
