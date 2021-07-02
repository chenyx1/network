package com.chenyx.socketchannel.multiplex;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ：chenyx
 * @description：
 * @date ：2021/6/30 9:31
 */
public interface IHandleEvent {

    /**
     * @desc connect事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    void connectEvent(SocketChannel socketChannel) throws IOException;

    /**
     * @desc accept事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    void acceptEvent(ServerSocketChannel serverSocketChannel) throws IOException;
    /**
     * @desc Read事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    void readEvent(SocketChannel socketChannel) throws IOException;

    /**
     * @desc write事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    void writeEvent(SocketChannel socketChannel) throws IOException;
}
