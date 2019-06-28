package ruleEntity.realTime;

import static utils.XMLParseUtil.parseXML;

import java.nio.file.LinkOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        
        getPathNode(systemComponent.getChannelList(), componentListAadl, pathNodeList);
        
        if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
		for (String pathNodeKeyString : pathNodeList.keySet()) {
			System.out.println(pathNodeKeyString + " " +
					pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " + 
					pathNodeList.get(pathNodeKeyString).getIsFirst() + " " + 
					pathNodeList.get(pathNodeKeyString).getWcet());
		}
		//getMaxPathWcet(pathNodeList);
        System.out.println("max wcet is " + getMaxPathWcet(pathNodeList));
	}
	
	private static void getPathNode(List<Channel> channelList,
			Map<String, Component> subComponentList,
			Map<String, PathNode> pathNodeList) {
		System.out.println(channelList.size());
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
			System.out.println("source id is " + sourceId + " dest id is " + destId);
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
		float maxPathWcet = 0;
		for (String pathNodeKeyString : pathNodeList.keySet()) {
			if (pathNodeList.get(pathNodeKeyString).getIsFirst()) {
				float maxPathWcetTemp = 0;
				float currentWcet = pathNodeList.get(pathNodeKeyString).getWcet();
				maxPathWcetTemp = calculateMaxWcet(maxPathWcetTemp, 
						pathNodeList.get(pathNodeKeyString).getNextComponents(),
						currentWcet);
				//System.out.println(maxPathWcet + "max path wcet temp is " + maxPathWcetTemp);
				if (maxPathWcet < maxPathWcetTemp) maxPathWcet = maxPathWcetTemp;
				//System.out.println("max path wcet is " + maxPathWcet);
			}
		}
		
		return maxPathWcet;
	}
	
	private static float calculateMaxWcet(float maxPathWcet, List<PathNode> pathNodeList, float currentWcet) {
		System.out.println(pathNodeList.size());
		if (pathNodeList.size() <= 0) {
			if (currentWcet > maxPathWcet) maxPathWcet = currentWcet;
			return maxPathWcet;
		} 
		for (PathNode pathNode : pathNodeList) {
			currentWcet += pathNode.getWcet();
			//System.out.println(pathNode.getId());
			//System.out.println("current wcet is " + currentWcet);
			System.out.println(pathNode.getNextComponents().size());
			maxPathWcet = calculateMaxWcet(maxPathWcet, pathNode.getNextComponents(), currentWcet);
			currentWcet -= pathNode.getWcet();
		}
		return maxPathWcet;
	}
	
	public static void main(String[] args) {
		excute();
	}
}
