package com.xiaoma.msgcenter;

import com.xiaoma.msgcenter.common.pojo.HelloWord;
import com.xiaoma.msgcenter.rpc.client.core.MessageSendExcutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyTest {

    @Autowired
    MessageSendExcutor messageSendExcutor;
    @Test
    public void test() {

        HelloWord helloWord1 = messageSendExcutor.execute(HelloWord.class);
        helloWord1.hello();
    }
}
