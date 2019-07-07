package ruleEntity.realTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathNode {

	//private Component component;
	private boolean isFirst;
	private List<PathNode> nextNodeList;
	private String id;
	private String name;
	private Map<String, Float> pathWcetList;
	float wcet = 0;
	
	public PathNode(String id, String wcet, String name, boolean isFirst) {
		this.id = id;
		this.isFirst = isFirst;
		this.name = name;
		//this.component = component;
		nextNodeList = new ArrayList<PathNode>();
		if (wcet != null) {
			this.wcet = Float.valueOf(wcet.substring(0, wcet.length() - 2));
		} else {
			this.wcet = 0;
			System.err.println("组件" + this.name + "没有wcet属性");
		}
		pathWcetList = new HashMap<String, Float>();

		if (name.equalsIgnoreCase("idle")) isFirst = true;
	}
	
	public String getId() {
		return id;
	}
	
	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
		if (name.equalsIgnoreCase("idle")) {
		    this.isFirst = true;
        }
	}
	
	public List<PathNode> getNextComponents() {
		return nextNodeList;
	}
	
	public boolean getIsFirst() {
		return isFirst;
	}
	
	public float getWcet() { 
		return wcet;
	}
	
	public void addPathWcet(String sourceId, float wcet) {
		float targetWcet = pathWcetList.get(sourceId);
		targetWcet += wcet;
		pathWcetList.put(sourceId, targetWcet);
	}
	
	public Map<String, Float> getPathWcet() {
		return pathWcetList;
	}

	public String getName() {
	    return name;
    }
	
//	public Component getComponent() {
//		return component;
//	}
}
