package org.chenyx.netty.simple.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @desc 客户端消息处理
 * @auhtor chenyx
 * @date 2021-08-01
 * */
public class TcpNettyClientHandler extends ChannelInboundHandlerAdapter {

    //读取事件触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx = " + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("server msg: " + buf.toString(CharsetUtil.UTF_8));
    }

    //建立连接触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StringBuilder msg = new StringBuilder();
        msg.append("hello server:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg.toString(), CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
