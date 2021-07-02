package com.chenyx.socketchannel.multiplex;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ：chenyx
 * @description：多路复用通道
 * @date ：2021/6/30 10:45
 */
public class MultiplexChannel extends Thread {

    protected Selector selector;

    private Boolean isStop = false;

    protected IHandleEvent handleEvent;

    public MultiplexChannel() {

    }

    public MultiplexChannel(IHandleEvent handleEvent) {
        this.handleEvent = handleEvent;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public IHandleEvent getHandleEvent() {
        return handleEvent;
    }

    public void setHandleEvent(IHandleEvent handleEvent) {
        this.handleEvent = handleEvent;
    }

    @Override
    public void run() {
        while (!isStop) {
            try {
                selector.select();//没有阻塞，直到注册在 Selector 中的 Channel 发送事件
                Set<SelectionKey> selectionKeys =  selector.selectedKeys();
                //事件监听，直接跳过后续执行
                if (selectionKeys == null || selectionKeys.size() == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator =  selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) {
                    selectionKey =  iterator.next();
                    iterator.remove();
                    try {
                        handleKey(selectionKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @desc 处理selectionKey
     * @auhtor chenyx
     * @date 2021-06-29
     *
     * */
    private void handleKey(SelectionKey selectionKey) throws IOException {
        if (selectionKey == null || !selectionKey.isValid()) {
            return;
        }
        if (selectionKey.isConnectable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel != null) {
                while (!socketChannel.finishConnect()) {
                    continue;
                }
                connectEvent(socketChannel);
                //注册read事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
        }
        //accept监听事件
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            //注册read事件
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                acceptEvent(serverSocketChannel);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
        }
        //read监听事件
        if (selectionKey.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel != null) {
                readEvent(socketChannel);
                //注册Write事件
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
        }

        //write监听事件
        if (selectionKey.isWritable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel != null) {
                writeEvent(socketChannel);
                //注册read事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
        }

    }

    /**
     * @desc connect事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    private void connectEvent(SocketChannel socketChannel) throws IOException {
        handleEvent.connectEvent(socketChannel);
    }

    /**
     * @desc accept事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    private void acceptEvent(ServerSocketChannel serverSocketChannel) throws IOException {
        handleEvent.acceptEvent(serverSocketChannel);
    }

    /**
     * @desc read事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    private void readEvent(SocketChannel socketChannel) throws IOException {
        handleEvent.readEvent(socketChannel);
    }

    /**
     * @desc write事件
     * @auhtor chenyx
     * @date 2021-06-30
     * */
    private void writeEvent(SocketChannel socketChannel) throws IOException {
        handleEvent.writeEvent(socketChannel);
    }


    /**
     * @desc  停止
     * @auhtor chenyx
     * @date 2021-06-29
     * */
    public void kill() {
        this.isStop = true;
    }
}
