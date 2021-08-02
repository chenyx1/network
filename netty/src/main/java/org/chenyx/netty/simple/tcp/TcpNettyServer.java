package org.chenyx.netty.simple.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/***
 * @desc 基于netty实现tcp服务端
 * @auhtor chenyx
 * @date 2021-08-02
 **/
public class TcpNettyServer {

    public static void main(String []args) throws InterruptedException {

        //booss处理请求连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //业务处理
        EventLoopGroup workGroup = new NioEventLoopGroup();

        final ServerBootstrap  serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workGroup)
                    //使用NioServerSocketChannel作为通道
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //创建通道初始化对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            channelPipeline.addLast(new TcpNettyServerHandler());
                        }
                    });

            //同步绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(9010).sync();
            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅关闭
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
