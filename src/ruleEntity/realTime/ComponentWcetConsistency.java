package ruleEntity.realTime;

import entity.*;

import java.util.*;

import static utils.XMLParseUtil.parseXML;

/*
* Author：lankx
* 手动验证文档，实时性规则，第一条
* */

public class ComponentWcetConsistency {

	
	public static void excute() {
		Map<String, PathNode> pathNodeList = new HashMap<String, PathNode>();
		Map<String, Component> componentListAadl = new LinkedHashMap<>();
        Map<String, Channel> channelListAadl = new LinkedHashMap<>();
        
        parseXML("aadl(9).xml", componentListAadl, channelListAadl);
//      System.out.println("\naadl存储的结果为：");
//		for (String componentKey : componentListAadl.keySet()) {
//			System.out.println("\nComponent id : " + componentKey);
//			componentListAadl.get(componentKey).attrsToString();
//			for (String subComponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
//				System.out.println("\nsubComponent id : " + subComponentKey);
//				componentListAadl.get(componentKey).getSubComponentList().get(subComponentKey).attrsToString();
//			}
//		}


		if (componentListAadl.get("system") != null)  {
			Component systemComponent = componentListAadl.get("system");
			float systemWcet = Float.valueOf(systemComponent.getAttr("wcet").substring(0,
					systemComponent.getAttr("wcet").length()-2));
			getSystemPathNode(systemComponent.getChannelList(), componentListAadl, pathNodeList);

			float targetWcet = getMaxPathWcet(pathNodeList);

			if (systemWcet < targetWcet) {
				System.out.println("系统" + systemComponent.getAttr("name") +
						"的wcet为" + systemWcet + "，其组件最长无循环路径的wcet为" + targetWcet +
						"，大于该系统的wcet，不满足实时性规则\n");
			} else {
				System.out.println("系统" + systemComponent.getAttr("name") +
						"的wcet为" + targetWcet + "，其组件最长无循环路径的wcet为" + targetWcet +
						"，不大于该系统的wcet，满足实时性规则\n");
			}
		}


        
        for (String componentKey : componentListAadl.keySet()) {
        	Component currentComponent = componentListAadl.get(componentKey);
        	if (componentListAadl.get(componentKey).getConnectionList().size() > 0) {
        		//System.out.println("has connections");
        		String wcetString = componentListAadl.get(componentKey).getAttr("wcet");
        		float wcet = Float.valueOf(wcetString.substring(0, wcetString.length()-2));
        		//System.out.println("wcet is " + wcet);
        		getPathNode(componentListAadl.get(componentKey).getConnectionList(), 
        				componentListAadl.get(componentKey).getSubComponentList(),
        				pathNodeList);
        		
        		//float maxPathWcet = getMaxPathWcet(pathNodeList);
        		float targetWcet = getMaxPathWcet(pathNodeList);
        		pathNodeList = new HashMap<>();

//        		System.out.println("复合组件" + componentListAadl.get(componentKey).getAttr("name") +
//                        " wcet为 " + wcet + " max path wcet is " + getMaxPathWcet(pathNodeList));

        		if (wcet < targetWcet) {
        			System.out.println("复合组件" + componentListAadl.get(componentKey).getAttr("name") +
							"的wcet为" + wcet + "，其子组件最长无循环路径的wcet为" + targetWcet +
							"，大于复合组件的wcet，不满足实时性规则\n");
				} else {
					System.out.println("复合组件" + componentListAadl.get(componentKey).getAttr("name") +
							"的wcet为" + wcet + "，其子组件最长无循环路径的wcet为" + targetWcet +
							"，不大于复合组件的wcet，满足实时性规则\n");
				}
        	}

        	if (currentComponent.getSubComponentList() != null) {
        		for (String subcomponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
        			Component subComponent = currentComponent.getSubComponentList().get(subcomponentKey);
        			if (subComponent.getAttr("type").equals("task") &&
							!subComponent.getConnectionList().isEmpty()) {
						String taskWcetString = subComponent.getAttr("wcet");
						float taskWcet = Float.valueOf(taskWcetString.substring(0, taskWcetString.length()-2));
						getPathNode(subComponent.getConnectionList(),
								subComponent.getSubComponentList(),
								pathNodeList);

						float targetWcet = getMaxPathWcet(pathNodeList);
						pathNodeList = new HashMap<>();

						//System.out.println("component id is " + subComponent.getAttr("id"));
						if (taskWcet < targetWcet) {
							System.out.println("复合组件" + subComponent.getAttr("name") +
									"的wcet为" + taskWcet + "，其子组件最长无循环路径的wcet为" + targetWcet +
									"，大于复合组件的wcet，不满足实时性规则\n");
						} else {
							System.out.println("复合组件" + subComponent.getAttr("name") +
									"的wcet为" + taskWcet + "，其子组件最长无循环路径的wcet为" + targetWcet +
									"，不大于复合组件的wcet，满足实时性规则\n");
						}
					}
				}
			}
        }
	}

	private static void getSystemPathNode(List<Channel> channelList,
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
				PathNode newDestNode = new PathNode(destId, subComponentList.get(destId).getAttr("wcet"),
						subComponentList.get(destId).getAttr("name"), false);
				pathNodeList.put(destId, newDestNode);
			}
			if (pathNodeList.get(sourceId) != null) {
				pathNodeList.get(sourceId).getNextComponents().add(pathNodeList.get(destId));
			} else {
				PathNode newSourceNode = new PathNode(sourceId, subComponentList.get(sourceId).getAttr("wcet"),
						subComponentList.get(sourceId).getAttr("name"), true);
				newSourceNode.getNextComponents().add(pathNodeList.get(destId));
				pathNodeList.put(sourceId, newSourceNode);
			}

		}
	}
	
	private static void getPathNode(List<Connection> connectionList,
			Map<String, Component> subComponentList,
			Map<String, PathNode> pathNodeList) {
		for (Connection connection : connectionList) {
			String sourceId = connection.getAttr("source"); 
			String destId = connection.getAttr("dest");
			for (String subComponentKey : subComponentList.keySet()) {
			    if (subComponentList.get(subComponentKey).getPortList().isEmpty()) continue;
			    for (Port port : subComponentList.get(subComponentKey).getPortList()) {
			        if (port.getAttr("id").equals(sourceId)) {
			            sourceId = subComponentKey;
			            break;
                    }
			        if (port.getAttr("id").equals(destId)) {
			            destId = subComponentKey;
			            break;
                    }
                }
            }
			if (subComponentList.get(destId) == null || subComponentList.get(sourceId) == null) continue;
			if (pathNodeList.get(destId) != null) pathNodeList.get(destId).setIsFirst(false);
			else {
				PathNode newDestNode = new PathNode(destId, subComponentList.get(destId).getAttr("wcet"),
						subComponentList.get(destId).getAttr("name"), false);
				pathNodeList.put(destId, newDestNode);
			}
			if (pathNodeList.get(sourceId) != null) {
				pathNodeList.get(sourceId).getNextComponents().add(pathNodeList.get(destId));
			} else {
				PathNode newSourceNode = new PathNode(sourceId, subComponentList.get(sourceId).getAttr("wcet"),
						subComponentList.get(sourceId).getAttr("name"), true);
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
					pathNodeIdStack.add(pathNode.getId());
					return maxPathWcet;
				}
			}
			pathNodeIdStack.add(pathNode.getId());
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
	
	public static void main(String[] args) {
		excute();
	}
}
