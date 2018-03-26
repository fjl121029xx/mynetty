package com.li.mynetty.netty.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServer {

    public static void main(String[] args) {

        //服务类
        ServerBootstrap bootstrap = new ServerBootstrap();

        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        //设置niococket工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));

        //设置管道的工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {

                ChannelPipeline pipeline = Channels.pipeline();
//                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addLast("hellohandler", new HelloHandler());

                return pipeline;
            }
        });

        //服务类绑定端口
        bootstrap.bind(new InetSocketAddress(10101));

    }

}

class HelloHandler extends SimpleChannelHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        System.out.println("messageReceived");
        /**
         * pipeline.addLast("decoder",new StringDecoder());
         * 可以注释掉下三行
         */
        ChannelBuffer message = (ChannelBuffer) e.getMessage();
        System.out.println(message);
        System.out.println(new String(message.array()));

//        System.out.println(e.getMessage());

        //回写
        ChannelBuffer hi = ChannelBuffers.copiedBuffer("hi".getBytes());
        ctx.getChannel().write(hi);

        super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

        System.out.println("exceptionCaught");
        super.exceptionCaught(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        System.out.println("channelConnected");
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        System.out.println("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }
}
