package com.xiaoma.msgcenter.common.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@Getter
@Setter
public class MethodResponse implements Serializable {
    private String messageId;
    private String error;
    private Object result;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
