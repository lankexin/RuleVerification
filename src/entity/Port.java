package entity;

import java.util.HashMap;
import java.util.Map;

public class Port {
    Map<String, String> attrs;
    /**
     * 存放一个组件的入接口和出接口的匹配关系
     */

    public Port() {
    	attrs = new HashMap<>();
    }
   
    public void setAttr(String key, String value) {
    	attrs.put(key, value);
    }
    
    public String getAttr(String key) {
    	return attrs.get(key);
    }
}
