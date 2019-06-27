package entity;

import java.util.HashMap;
import java.util.Map;

public class Connection {
	
	private Map<String, String> attrsMap;
	
	public Connection() {
		attrsMap = new HashMap<String, String>();
	}
	
	public void setAttr(String key, String value) {
		attrsMap.put(key, value);
	}
	
	public String getAttr(String key) {
		return attrsMap.get(key);
	}
	
	public void attrsToString() {
    	for (String attrKey : attrsMap.keySet()) {
    		System.out.println(attrKey + " " + attrsMap.get(attrKey));
    	}
    }
}
