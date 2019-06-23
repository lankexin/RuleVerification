package entity;

import java.util.HashMap;
import java.util.Map;

public class Linkpoint {
    Map<String, String> attrs;
    /**
     * 存放一个组件的入接口和出接口的匹配关系
     */
//    String postcondition;
//    Long period;

    public Linkpoint() {
    	attrs = new HashMap<>();
    }
   
    public void setAttr(String key, String value) {
    	attrs.put(key, value);
    }
    
    public String getAttr(String key) {
    	return attrs.get(key);
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPrecondition() {
//        return precondition;
//    }
//
//    public void setPrecondition(String precondition) {
//        this.precondition = precondition;
//    }

//    public String getPostcondition() {
//        return postcondition;
//    }
//
//    public void setPostcondition(String postcondition) {
//        this.postcondition = postcondition;
//    }
//
//    public Long getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(Long period) {
//        this.period = period;
//    }
    
//    public String getId() {
//    	return id;
//    }
//    
//    public String getDirection() {
//    	return direction;
//    }
//
//    public void setDirection(String direction) {
//    	this.direction = direction;
//    }
//    
//    public void setId(String id) {
//    	this.id = id;
//    }
}
