package entity;

import java.util.HashMap;
import java.util.Map;

public class Transition {

	private Map<String, String> attrs;
	
	public Transition() {
		attrs = new HashMap<>();
	}
	
	public void setAttr(String key, String value) {
		attrs.put(key, value);
	}
	
	public String getAttr(String key) {
    	return attrs.get(key);
    }
	
}
