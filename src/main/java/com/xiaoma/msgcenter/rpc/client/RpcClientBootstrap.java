package com.xiaoma.msgcenter.rpc.client;

import com.xiaoma.msgcenter.rpc.client.handler.RpcSendHandler;
import com.xiaoma.msgcenter.rpc.server.handler.RpcRcvHandler;
import com.xiaoma.msgcenter.serialize.protostuff.ProtostuffCodecUtil;
import com.xiaoma.msgcenter.serialize.protostuff.ProtostuffDecoder;
import com.xiaoma.msgcenter.serialize.protostuff.ProtostuffEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class RpcClientBootstrap {
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private InetSocketAddress inetSocketAddress = new InetSocketAddress("10.20.72.168", 1234);

    @Autowired
    private RpcSendHandler rpcSendHandler;

    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        ProtostuffCodecUtil util = new ProtostuffCodecUtil();
                        util.setRpcDirect(false);
                        pipeline.addLast(new ProtostuffEncoder(util));
                        pipeline.addLast(new ProtostuffDecoder(util));
//                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                        pipeline.addLast(new LengthFieldPrepender(4));
//                        pipeline.addLast(new ObjectEncoder());
//                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        pipeline.addLast(rpcSendHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    //MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                    //RpcServerLoader.getInstance().setMessageSendHandler(handler);
                    System.out.println("---------------------client connect server");
                }
            }
        });
    }
}
