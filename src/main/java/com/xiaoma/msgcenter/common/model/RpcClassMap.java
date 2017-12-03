package com.xiaoma.msgcenter.common.model;

import com.xiaoma.msgcenter.common.pojo.HelloWord;
import com.xiaoma.msgcenter.common.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component("rpcClassMap")
@ConfigurationProperties(prefix = "rpc")
@DependsOn(value="springUtil")
public class RpcClassMap {
    private Map<String, String> nameMap;

    private Map<String, Object> objectMap = new HashMap<>();

    @PostConstruct
    public void init() {
        nameMap.forEach((k, v)->nameMapToObjectMap(k, v));
    }

    private void nameMapToObjectMap(String key, String objectName) {
        //System.out.println("key: " + key + "  value: " + objectName);
        objectMap.put(key, SpringUtil.getBean(key));
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public Object getObject(String objectName) {
        Object object = objectMap.get(objectName);
        return object;
    }

}
