package entity;

import java.util.HashMap;
import java.util.Map;

public class ErrorPropagations {
	
	Map<String, String> attrs;
	Map<String, Propagation> propagationList;

	public ErrorPropagations() {
		attrs = new HashMap<String, String>();
	}
	
	public Map<String, Propagation> getPropagationList() {
		return propagationList;
	}
	
	public Map<String, String> getAttrs() {
		return attrs;
	}
	
	public String getAttr(String key) {
		return attrs.get(key);
	} 
	
	public void setAttr(String key, String value) {
		attrs.put(key, value);
	}
	
	public void attrsToString() {
    	for (String attrKey : attrs.keySet()) {
    		System.out.println(attrKey + " " + attrs.get(attrKey));
    	}
    }
}
