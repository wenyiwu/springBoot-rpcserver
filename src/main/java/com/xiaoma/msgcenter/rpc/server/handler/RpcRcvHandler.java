package com.xiaoma.msgcenter.rpc.server.handler;

import com.xiaoma.msgcenter.common.model.MethodRequest;
import com.xiaoma.msgcenter.common.model.MethodResponse;
import com.xiaoma.msgcenter.common.model.RpcClassMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.reflect.MethodUtils;


public class RpcRcvHandler extends ChannelInboundHandlerAdapter {

    private RpcClassMap rpcClassMap;

    public RpcRcvHandler(RpcClassMap rpcClassMap) {
        this.rpcClassMap = rpcClassMap;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        MethodRequest request = (MethodRequest) msg;
        MethodResponse response = new MethodResponse();
        response.setMessageId(request.getMethodId());
        try {
            Object result = reflect(request);
            response.setResult(result);
            System.out.println(result);
        } catch (Throwable t) {
            response.setError(t.toString());
            t.printStackTrace();
            System.err.printf("RPC Server invoke error!\n");
        }
        ctx.writeAndFlush(response);

        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    private Object reflect(MethodRequest request) throws Throwable {
        Object serverBean = rpcClassMap.getObject(request.getClassName());
        String methodName = request.getMethodName();
        Object[] params = request.getParams();
        return MethodUtils.invokeMethod(serverBean, methodName, params);
    }
}
