package org.chenyx.netty.simple.tcp;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @desc TCP客户端
 * @author chenyx
 * @date 2021-08-02
 * */
public class TcpNettyClient {

    public static void main(String []args) throws InterruptedException {

        //客户端时间处理循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //客户端环境配置
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TcpNettyClientHandler());
                        }
                    });
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9010).sync();

            //监听连接关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅关机
            eventLoopGroup.shutdownGracefully();
        }
    }
}
