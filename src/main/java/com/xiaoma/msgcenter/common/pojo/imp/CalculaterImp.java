package com.xiaoma.msgcenter.common.pojo.imp;

import com.xiaoma.msgcenter.common.pojo.Calculater;
import org.springframework.stereotype.Component;

@Component("Calculater")
public class CalculaterImp implements Calculater {
    @Override
    public Integer add(int a, int b) {
        return a + b;
    }
}
