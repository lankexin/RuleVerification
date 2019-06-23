package entity;

import java.util.HashMap;
import java.util.Map;

public class Channel {
	
	Map<String, String> attrs;

	public Channel() {
		attrs = new HashMap<>();
	}
	
	
	public void setAttr(String key,  String value) {
		attrs.put(key, value);
	}
	
	public String getAttr(String key) {
		return attrs.get(key);
	}
}
