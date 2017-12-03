package com.xiaoma.msgcenter.rpc.client.core;

import com.google.common.reflect.AbstractInvocationHandler;
import com.xiaoma.msgcenter.common.model.MethodRequest;
import com.xiaoma.msgcenter.common.model.RpcClassMap;
import com.xiaoma.msgcenter.common.pojo.HelloWord;
import com.xiaoma.msgcenter.rpc.client.handler.RpcSendHandler;
import io.netty.util.concurrent.Future;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.UUID;

@Service
public class MessageSynSendProxy<T> extends AbstractInvocationHandler {

    @Autowired
    private RpcSendHandler rpcSendHandler;

    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        MethodRequest request = new MethodRequest();
        request.setMethodId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getSimpleName());
        request.setMethodName(method.getName());
        request.setParamsType(method.getParameterTypes());
        request.setParams(args);
        return rpcSendHandler.sendRequest(request);
        //return callBack.start();
        //HelloTest helloTest = new HelloTest();
//        String className = method.getDeclaringClass().getSimpleName();
//        Object serviceBean = rpcClassMap.getObject(className);
//        String methodName = method.getName();
//        Object[] parameters = args;
//        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }
}