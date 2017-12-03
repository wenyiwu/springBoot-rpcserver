package com.xiaoma.msgcenter;

import com.xiaoma.msgcenter.common.model.MethodRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReflectionToStringBuilderTest {

    @Test
    public void test() {
        MethodRequest methodRequest = new MethodRequest();
        methodRequest.setClassName("123");
        methodRequest.setMethodName("321");
        methodRequest.setMethodId("1");
        System.out.println(methodRequest);
    }
}
