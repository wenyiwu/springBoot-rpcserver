package com.xiaoma.msgcenter.rpc.server;

import com.xiaoma.msgcenter.common.handler.InvokerHandler;
import com.xiaoma.msgcenter.common.model.RpcClassMap;
import com.xiaoma.msgcenter.common.pool.ThreadPoolService;
import com.xiaoma.msgcenter.rpc.server.handler.RpcRcvHandler;
import com.xiaoma.msgcenter.serialize.protostuff.ProtostuffCodecUtil;
import com.xiaoma.msgcenter.serialize.protostuff.ProtostuffDecoder;
import com.xiaoma.msgcenter.serialize.protostuff.ProtostuffEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class RpcServerBootstrap {
    public static final int SYSTEM_PROPERTY_PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    @Autowired
    private RpcClassMap rpcClassMap;

    private EventExecutorGroup e1 = new DefaultEventExecutorGroup(SYSTEM_PROPERTY_PARALLEL*2);

    @PostConstruct
    public void start() throws Exception {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channelFactory(NioServerSocketChannel::new)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        ChannelPipeline pipeline = socketChannel.pipeline();
                        ProtostuffCodecUtil util = new ProtostuffCodecUtil();
                        util.setRpcDirect(true);
                        pipeline.addLast(e1,new ProtostuffEncoder(util));
                        pipeline.addLast(e1,new ProtostuffDecoder(util));
//                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                        pipeline.addLast("ObjectDecoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
//                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                        pipeline.addLast("ObjectEncoder", new ObjectEncoder());
                        pipeline.addLast("idleStateHandler", new IdleStateHandler(300, 0, 0));
                        pipeline.addLast(e1, "rpcRcvHandler", new RpcRcvHandler(rpcClassMap));
                        //pipeline.addLast("invoker", new InvokerHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_SNDBUF, 2048)
                .option(ChannelOption.SO_RCVBUF, 1024);
        bootstrap.bind(1234).sync();
        //log.info("Node server start successful! listening port: {}", goPushNodeServerConfig.getNodePort());
        System.out.println("Node server start successful!");
    }


    @PreDestroy
    public void destory() {
        //log.info("Node Server will be stoped!");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
