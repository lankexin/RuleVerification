package entity;

import java.util.HashMap;
import java.util.Map;

public class ExceptionXML {
	private Map<String, String> attrs;
	
	public ExceptionXML() {
		attrs = new HashMap<>();
	}
	
	public void setAttr(String key, String value) {
		attrs.put(key, value);
	}
	
	public String getAttr(String key) {
    	return attrs.get(key);
    }
	
	public void attrsToString() {
    	for (String attrKey : attrs.keySet()) {
    		System.out.println(attrKey + " " + attrs.get(attrKey));
    	}
    }
}
