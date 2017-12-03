package com.xiaoma.msgcenter.common.handler;

import com.xiaoma.msgcenter.common.model.MethodRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerHandler extends ChannelInboundHandlerAdapter {
    public static ConcurrentHashMap<String, Object> classMap = new ConcurrentHashMap<String,Object>();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MethodRequest methodRequest = (MethodRequest)msg;
        Object claszz = null;
        //用于记录反射获得类，这样可以提高性能
        if(!classMap.containsKey(methodRequest.getClassName())){
            try {
                claszz = Class.forName(methodRequest.getClassName()).newInstance();
                classMap.put(methodRequest.getClassName(), claszz);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            claszz = classMap.get(methodRequest.getClassName());
        }
        Method method = claszz.getClass().getMethod(methodRequest.getMethodName(), methodRequest.getParamsType());
        Object result = method.invoke(claszz, methodRequest.getParams());
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}