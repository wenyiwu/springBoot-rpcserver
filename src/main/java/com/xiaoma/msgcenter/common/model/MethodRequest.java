package com.xiaoma.msgcenter.common.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Getter
@Setter
public class MethodRequest implements Serializable {
    private String methodId;
    private String className;
    private String methodName;
    private Class[] paramsType;
    private Object[] params;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, new String[]{"paramsType", "params"});
    }
}
