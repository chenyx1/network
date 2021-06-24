package com.chenyx.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author ：chenyx
 * @description：SocketChannel
 * @date ：2021/6/24 11:05
 */
public class SimpleSocketChannelDemo1 {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress local = new InetSocketAddress(9012);
        socketChannel.bind(local);
        SocketAddress remote = new InetSocketAddress("127.0.0.1",9011);
        socketChannel.configureBlocking(false);
        socketChannel.connect(remote);
        try {
            if (!socketChannel.finishConnect()) {
                System.out.println("connecting......");
            } else {
                //发请求
                ByteBuffer src = ByteBuffer.allocate(512);
                src.put("hello world".getBytes());
                src.flip();//模式切换
                while (src.hasRemaining()){
                    int res = socketChannel.write(src);
                    System.out.println("res:" + res);
                }

                //暂停2s
                Thread.sleep(2000);

                ByteBuffer buffer = ByteBuffer.allocate(512);
                //接受请求
                socketChannel.read(buffer);
                buffer.flip();//切换模式
                String msg = new String(buffer.array());
                System.out.println("serverSocketChannel:" + socketChannel.getRemoteAddress() + ",msg:" + msg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
