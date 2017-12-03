package com.xiaoma.msgcenter.rpc.client.handler;

import com.xiaoma.msgcenter.common.model.MethodRequest;
import com.xiaoma.msgcenter.common.model.MethodResponse;
import com.xiaoma.msgcenter.rpc.client.core.MessageCallBack;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class RpcSendHandler extends SimpleChannelInboundHandler {

    private ConcurrentHashMap<String, MessageCallBack> callBackMap = new ConcurrentHashMap<>();

    private volatile Channel channel;

    private Lock lock = new ReentrantLock();

    private Condition finish = lock.newCondition();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        MethodResponse response = (MethodResponse)msg;
        String messageId = response.getMessageId();
        MessageCallBack messageCallBack = callBackMap.get(messageId);
        if(messageCallBack != null) {
            callBackMap.remove(messageId);
            messageCallBack.over(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //log.error("exception error:{}, channel:{}", cause, ctx.channel());
        ctx.close();
    }

    public Object sendRequest(MethodRequest request) throws InterruptedException {
        MessageCallBack messageCallBack = new MessageCallBack(request);
        callBackMap.put(request.getMethodId(), messageCallBack);

        channel.writeAndFlush(request);
        return messageCallBack.start();
    }

    public Object sendAsynRequest(MethodRequest request) throws InterruptedException {
        MessageCallBack messageCallBack = new MessageCallBack(request);
        callBackMap.put(request.getMethodId(), messageCallBack);

        channel.writeAndFlush(request);
        return messageCallBack.start();
    }
}
