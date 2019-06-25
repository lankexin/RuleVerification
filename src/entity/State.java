package entity;

import java.util.*;

public class State {
    private Map<String, String> attrs;
    private List<State> subStateList;

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
