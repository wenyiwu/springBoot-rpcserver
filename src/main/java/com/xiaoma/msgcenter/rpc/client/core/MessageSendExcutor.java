package com.xiaoma.msgcenter.rpc.client.core;

import com.google.common.reflect.Reflection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSendExcutor {
    @Autowired
    MessageSynSendProxy messageSendProxy;

    public <T> T execute(Class<T> rpcInterface) {
        return (T) Reflection.newProxy(rpcInterface, messageSendProxy);
    }
}
