package com.xiaoma.msgcenter.rpc.client.core;

import com.google.common.reflect.AbstractInvocationHandler;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MessageAsynSendProxy<T> extends AbstractInvocationHandler {
    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

}
