package com.xiaoma.msgcenter.common.pojo.imp;

import com.xiaoma.msgcenter.common.pojo.HelloWord;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("HelloWord")
public class HelloWordImp implements HelloWord{

    @Override
    public void hello() {
        System.out.println("Hello word");
    }
}
