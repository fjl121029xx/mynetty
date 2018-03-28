package com.li.mynetty.netty.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {

    public static void main(String[] args) {

        //客户端
        ClientBootstrap clientBootstrap = new ClientBootstrap();

        //线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        //socket工厂
        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));

        //管道工厂
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {

                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringDecoder());
                pipeline.addLast("mHandler", new MesHandler());

                return pipeline;
            }
        });

        ChannelFuture future = clientBootstrap.connect(new InetSocketAddress("127.0.0.1", 10101));

        Channel channel = future.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (true) {

            channel.write( ChannelBuffers.copiedBuffer(scanner.next().getBytes()));
        }


    }
}

class MesHandler extends SimpleChannelHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        System.out.println("messageReceived");
        /**
         * pipeline.addLast("decoder",new StringDecoder());
         * 可以注释掉下三行
         */
//        ChannelBuffer message = (ChannelBuffer) e.getMessage();
//        System.out.println(message);
//        System.out.println(new String(message.array()));

        System.out.println(e.getMessage());

        //回写
        /*ChannelBuffer hi = ChannelBuffers.copiedBuffer("hi".getBytes());
        ctx.getChannel().write(hi);*/

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
