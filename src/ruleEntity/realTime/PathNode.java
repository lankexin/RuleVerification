package ruleEntity.realTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Component;

public class PathNode {

	private Component component;
	private boolean isFirst;
	private List<PathNode> nextNodeList;
	private String id;
	private Map<String, Float> pathWcetList;
	float wcet = 0;
	
	public PathNode(String id, Component component, boolean isFirst) {
		this.id = id;
		this.isFirst = isFirst;
		this.component = component;
		nextNodeList = new ArrayList<PathNode>();
		String wcetString = component.getAttr("wcet");
		wcet = Float.valueOf(wcetString.substring(0, wcetString.length()-2));
		pathWcetList = new HashMap<String, Float>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
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
	
	public Component getComponent() {
		return component;
	}
}
