package com.chenyx.socketchannel.multiplex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ：chenyx
 * @description 多路复用SocketChannel
 * @date ：2021/6/30 9:39
 */
public class MultiplexSocketChannelDemo1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        IHandleEvent handleEvent = new SocketChannnelHandleEvent();
        SocketAddress remote = new InetSocketAddress("127.0.0.1",9013);
        MultiplexSocketChannel multiplexSocketChannel = new MultiplexSocketChannelDemo1()
                .new MultiplexSocketChannel(9014,handleEvent,remote);
        multiplexSocketChannel.start();
        multiplexSocketChannel.sendMsg("hello");
    }

    public class MultiplexSocketChannel extends MultiplexChannel {

        private SocketChannel socketChannel;

        public MultiplexSocketChannel(int port, IHandleEvent handleEvent,SocketAddress remote) {
            try {
                this.handleEvent = handleEvent;
                socketChannel = SocketChannel.open();
                SocketAddress local = new InetSocketAddress(port);
                socketChannel.bind(local);
                socketChannel.configureBlocking(false);
                selector = Selector.open();
                //监听accept事件
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                socketChannel.connect(remote);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @desc 向服务端发送消息
         * @auhtor chenyx
         * @date 2021-06-30
         * */
        public void sendMsg(String msg) throws IOException, InterruptedException {
            while (!socketChannel.finishConnect()) {
                continue;
            }
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(msg.getBytes());
            buffer.flip();//切换读模式
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        }
    }
}
