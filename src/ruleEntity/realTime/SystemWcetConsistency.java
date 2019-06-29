package ruleEntity.realTime;

import static utils.XMLParseUtil.parseXML;

import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

import entity.Channel;
import entity.Component;
import entity.Connection;
import entity.Linkpoint;

public class SystemWcetConsistency {
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
        
        if (componentListAadl.get("system") == null)  return;
        
        Component systemComponent = componentListAadl.get("system");
        float systemWcet = Float.valueOf(systemComponent.getAttr("wcet").substring(0,
        		systemComponent.getAttr("wcet").length()-2));
        System.out.println("system wcet is " + systemWcet);
        getPathNode(systemComponent.getChannelList(), componentListAadl, pathNodeList);
        
//        if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
//		for (String pathNodeKeyString : pathNodeList.keySet()) {
//			System.out.println(pathNodeKeyString + " " +
//					pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " + 
//					pathNodeList.get(pathNodeKeyString).getIsFirst() + " " + 
//					pathNodeList.get(pathNodeKeyString).getWcet());
//		}
		//getMaxPathWcet(pathNodeList);
        System.out.println("max wcet is " + getMaxPathWcet(pathNodeList));
	}
	
	private static void getPathNode(List<Channel> channelList,
			Map<String, Component> subComponentList,
			Map<String, PathNode> pathNodeList) {
		//System.out.println(channelList.size());
		for (Channel channel : channelList) {
			String sourceId = channel.getAttr("source"); 
			for (String componentKey : subComponentList.keySet()) {
				if (subComponentList.get(componentKey).getLinkpointList().size() > 0) {
					for (Linkpoint linkpoint : subComponentList.get(componentKey).getLinkpointList()) {
						if (linkpoint.getAttr("id").equals(sourceId)) {
							sourceId = subComponentList.get(componentKey).getAttr("id");
							break;
						}
					}
				}
			}
			String destId = channel.getAttr("dest");
			for (String componentKey : subComponentList.keySet()) {
				if (subComponentList.get(componentKey).getLinkpointList().size() > 0) {
					for (Linkpoint linkpoint : subComponentList.get(componentKey).getLinkpointList()) {
						if (linkpoint.getAttr("id").equals(destId)) {
							destId = subComponentList.get(componentKey).getAttr("id");
							break;
						}
					}
				}
			}
			//System.out.println("source id is " + sourceId + " dest id is " + destId);
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
		System.out.println(pathNodeList.size());
		Stack<String> pathNodeIdList = new Stack<String>();
		float maxPathWcet = 0;
		for (String pathNodeKeyString : pathNodeList.keySet()) {
			if (pathNodeList.get(pathNodeKeyString).getIsFirst()) {
				float maxPathWcetTemp = 0;
				float currentWcet = pathNodeList.get(pathNodeKeyString).getWcet();
				pathNodeIdList.add(pathNodeList.get(pathNodeKeyString).getId());
				maxPathWcetTemp = calculateMaxWcet(maxPathWcetTemp, 
						pathNodeList.get(pathNodeKeyString),
						currentWcet, pathNodeIdList);
				//System.out.println(maxPathWcet + "max path wcet temp is " + maxPathWcetTemp);
				if (maxPathWcet < maxPathWcetTemp) maxPathWcet = maxPathWcetTemp;
				pathNodeIdList.clear();
				//System.out.println("max path wcet is " + maxPathWcet);
			}
		}
		
		return maxPathWcet;
	}
	
	private static float calculateMaxWcet(float maxPathWcet, 
			PathNode pathNode, 
			float currentWcet,
			Stack<String> pathNodeIdStack) {
//		System.out.println("node name is " + pathNode.getComponent().getAttr("name"));
//		System.out.println(pathNodeIdStack.size());
		//System.out.println(pathNodeList.size());
		if (pathNode.getNextComponents().isEmpty()) {
			if (currentWcet > maxPathWcet) maxPathWcet = currentWcet;
			//System.out.println("no next component and max path wcet is " + maxPathWcet);
			return maxPathWcet;
		} 
		//System.out.println("current wcet is " + currentWcet);
		
		if (pathNodeIdStack.size() > 1) {
			//System.out.println("first element is " + pathNodeIdStack.peek() + " stack size is " + pathNodeIdStack.size());
			pathNodeIdStack.pop();
			//System.out.println(pathNodeIdStack.size());
			for (String id : pathNodeIdStack) {
				//System.out.println(id);
				if (pathNode.getId().equals(id)) {
//					System.out.println(pathNode.getId() + 
//							" already in path");
					return maxPathWcet;
				}
			}
			pathNodeIdStack.add(pathNode.getId());
		}
		//if (currentWcet > 4) return maxPathWcet;
		for (PathNode nextPathNode : pathNode.getNextComponents()) {
			currentWcet += nextPathNode.getWcet();
			pathNodeIdStack.add(nextPathNode.getId());
			//System.out.println(pathNode.getId());
			//System.out.println("current wcet is " + currentWcet);
//			System.out.println(nextPathNode.getNextComponents().size() + 
//					" wcet is " + nextPathNode.getWcet());
			maxPathWcet = calculateMaxWcet(maxPathWcet, nextPathNode, currentWcet, pathNodeIdStack);
			currentWcet -= nextPathNode.getWcet();
			pathNodeIdStack.pop();
		}
		return maxPathWcet;
	}
	
	public static void main(String[] args) {
		excute();
	}
}
