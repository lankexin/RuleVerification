package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private Map<String, String> attrs;
    private List<State> subStateList;
    List<Transition> transitionList;

    public State() {
    	attrs = new HashMap<>();
    	subStateList = new ArrayList<>();
    	transitionList = new ArrayList<>();
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

    public List<Transition> getTransitionList() {
        return transitionList;
    }

    public void attrsToString() {
    	for (String attrKey : attrs.keySet()) {
    		System.out.println(attrKey + " " + attrs.get(attrKey));
    	}
    }
}
