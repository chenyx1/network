package com.chenyx.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author ：chenyx
 * @description：多复用Nio
 * @date ：2021/6/29 11:00
 */
public class MultiplexServerSocketChancnelDemo1 {

    public static void main(String[] args) {

    }

    /**
     * @desc 多复用ServerSocketChancnel
     * @author chenyx
     * @date 2021-06-29
     * */
    class  MultiplexServerSocketChancnel  implements Runnable {

        private Selector selector;

        private Boolean isStop = false;

        public MultiplexServerSocketChancnel(int port) {
            try {
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
                    Iterator<SelectionKey>  iterator =  selectionKeys.iterator();
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
            //accept监听事件
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                //监听read事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            //read监听事件
            if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                //监听Write事件
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }

            //write监听事件
            if (selectionKey.isWritable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            }

        }


        /**
         * @desc  停止
         * @auhtor chenyx
         * @date 2021-06-29
         * */
       public void stop() {
            this.isStop = true;
        }
    }
}
