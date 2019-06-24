package entity;

import java.util.*;

public class State {
    private static Map<String, String> attrs;
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

    public static List<String> attrsList(){
        List<String> attrsList=new ArrayList<>();
        Set<String> componentSet = attrs.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            attrsList.add(str);
        }
        return attrsList;
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
