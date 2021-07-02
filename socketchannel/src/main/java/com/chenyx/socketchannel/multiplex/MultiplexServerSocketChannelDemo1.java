package com.chenyx.socketchannel.multiplex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author ：chenyx
 * @description：多复用Nio
 * @date ：2021/6/29 11:00
 */
public class MultiplexServerSocketChannelDemo1 {

    public static void main(String[] args) {

        IHandleEvent handleEvent = new ServerSocketChannnelHandleEvent();
        MultiplexServerSocketChancnel multiplexServerSocketChancnel = new MultiplexServerSocketChannelDemo1()
                .new MultiplexServerSocketChancnel(9013, handleEvent);
        multiplexServerSocketChancnel.start();
    }

    /**
     * @desc 多复用ServerSocketChancnel
     * @author chenyx
     * @date 2021-06-29
     * */
    class  MultiplexServerSocketChancnel extends MultiplexChannel {
        public MultiplexServerSocketChancnel(int port, IHandleEvent handleEvent) {
            try {
                this.handleEvent = handleEvent;
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                SocketAddress local = new InetSocketAddress(port);
                serverSocketChannel.bind(local);
                serverSocketChannel.configureBlocking(false);
                selector = Selector.open();
                //监听accept事件
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
