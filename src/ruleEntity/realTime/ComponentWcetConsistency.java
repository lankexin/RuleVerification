package ruleEntity.realTime;

import static utils.XMLParseUtil.parseXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.sun.org.apache.regexp.internal.recompile;

import entity.Channel;
import entity.Component;
import entity.Connection;

public class ComponentWcetConsistency {

	
	public static void excute() {
		Map<String, PathNode> pathNodeList = new HashMap<String, PathNode>();
		Map<String, Component> componentListAadl = new LinkedHashMap<>();
        Map<String, Channel> channelListAadl = new LinkedHashMap<>();
        
        parseXML("aadl(4).xml", componentListAadl, channelListAadl);
//      System.out.println("\naadl存储的结果为：");
//		for (String componentKey : componentListAadl.keySet()) {
//			System.out.println("\nComponent id : " + componentKey);
//			componentListAadl.get(componentKey).attrsToString();
//			for (String subComponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
//				System.out.println("\nsubComponent id : " + subComponentKey);
//				componentListAadl.get(componentKey).getSubComponentList().get(subComponentKey).attrsToString();
//			}
//		}
        
        for (String componentKey : componentListAadl.keySet()) {
//        	System.out.println(componentKey + " connect list size is " +
//        						componentListAadl.get(componentKey).getConnectionList().size());
        	if (componentListAadl.get(componentKey).getConnectionList().size() > 0) {
        		System.out.println("has connections");
        		String wcetString = componentListAadl.get(componentKey).getAttr("wcet");
        		float wcet = Float.valueOf(wcetString.substring(0, wcetString.length()-2));
        		//System.out.println("wcet is " + wcet);
        		getPathNode(componentListAadl.get(componentKey).getConnectionList(), 
        				componentListAadl.get(componentKey).getSubComponentList(),
        				pathNodeList);
        		
        		//float maxPathWcet = getMaxPathWcet(pathNodeList);
        		getMaxPathWcet(pathNodeList);
        		System.out.println("max path wcet is " + getMaxPathWcet(pathNodeList));
//        		if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
//        		for (String pathNodeKeyString : pathNodeList.keySet()) {
//        			System.out.println(pathNodeKeyString + " " +
//        					pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " + 
//        					pathNodeList.get(pathNodeKeyString).getIsFirst() + " " + 
//        					pathNodeList.get(pathNodeKeyString).getWcet());
//        		}
        	}
        }
	}
	
	private static void getPathNode(List<Connection> connectionList,
			Map<String, Component> subComponentList,
			Map<String, PathNode> pathNodeList) {
		for (Connection connection : connectionList) {
			String sourceId = connection.getAttr("source"); 
			String destId = connection.getAttr("dest");
			if (subComponentList.get(destId) == null || subComponentList.get(sourceId) == null) continue;
			if (pathNodeList.get(destId) != null) pathNodeList.get(destId).setIsFirst(false);
			else {
				PathNode newDestNode = new PathNode(destId, subComponentList.get(destId), false);
				pathNodeList.put(destId, newDestNode);
			}
			if (pathNodeList.get(sourceId) != null) {
				pathNodeList.get(sourceId).getNextComponents().add(pathNodeList.get(destId));
			} else {
				PathNode newSourceNode = new PathNode(sourceId, subComponentList.get(sourceId), true);
				newSourceNode.getNextComponents().add(pathNodeList.get(destId));
				pathNodeList.put(sourceId, newSourceNode);
			}
			
		}
	}
	
	private static float getMaxPathWcet(Map<String, PathNode> pathNodeList) {
		Stack<String> pathNodeIdStack = new Stack<String>();
		float maxPathWcet = 0;
		for (String pathNodeKeyString : pathNodeList.keySet()) {
			if (pathNodeList.get(pathNodeKeyString).getIsFirst()) {
				float maxPathWcetTemp = 0;
				float currentWcet = pathNodeList.get(pathNodeKeyString).getWcet();
				pathNodeIdStack.add(pathNodeList.get(pathNodeKeyString).getId());
				maxPathWcetTemp = calculateMaxWcet(maxPathWcetTemp, pathNodeList.get(pathNodeKeyString),
						currentWcet, pathNodeIdStack);
				//System.out.println(maxPathWcet + "max path wcet temp is " + maxPathWcetTemp);
				if (maxPathWcet < maxPathWcetTemp) maxPathWcet = maxPathWcetTemp;
				pathNodeIdStack.clear();
			}
		}
		
		return maxPathWcet;
	}
	
	private static float calculateMaxWcet(float maxPathWcet, 
			PathNode pathNode, 
			float currentWcet,
			Stack<String> pathNodeIdStack) {
		if (pathNode.getNextComponents().size() <= 0) {
			//System.err.println(maxPathWcet + " current wcet is " + currentWcet);
			if (currentWcet > maxPathWcet) maxPathWcet = currentWcet;
			//System.err.println("max wcet is " + maxPathWcet);
			return maxPathWcet;
		} 
		
		if (pathNodeIdStack.size() > 1) {
			pathNodeIdStack.pop();
			for (String id : pathNodeIdStack) {
				if (pathNode.getId().equals(id)) {
					return maxPathWcet;
				}
			}
		}
		for (PathNode nextPathNode : pathNode.getNextComponents()) {
			currentWcet += nextPathNode.getWcet();
			pathNodeIdStack.add(nextPathNode.getId());
			//System.out.println(pathNode.getId());
			//System.out.println("current wcet is " + currentWcet);
			maxPathWcet = calculateMaxWcet(maxPathWcet, nextPathNode, currentWcet, pathNodeIdStack);
			currentWcet -= nextPathNode.getWcet();
			pathNodeIdStack.pop();
		}
		return maxPathWcet;
	}
	
	public static void main(String[] atgs) {
		excute();
	}
}
