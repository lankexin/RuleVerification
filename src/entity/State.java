package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private Map<String, String> attrs;
    private List<State> subStateList;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public List<Fault> getFaultList() {
//        return faultList;
//    }
//
//    public void setFaultList(List<Fault> faultList) {
//        this.faultList = faultList;
//    }
    
    public State() {
    	attrs = new HashMap<>();
    	subStateList = new ArrayList<>();
    }
    
    public void setAttr(String key, String value) {
    	attrs.put(key, value);
    }
    
    public String getAttr(String key) {
    	return attrs.get(key);
    }
    
    public List<State> getSubStateList() {
    	return subStateList;
    }
    
    public void attrsToString() {
    	for (String attrKey : attrs.keySet()) {
    		System.out.println(attrKey + " " + attrs.get(attrKey));
    	}
    }
}
