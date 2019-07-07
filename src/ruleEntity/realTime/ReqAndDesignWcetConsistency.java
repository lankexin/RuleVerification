package ruleEntity.realTime;

import entity.Channel;
import entity.Component;
import entity.Connection;
import entity.Linkpoint;

import java.util.*;

import static utils.XMLParseUtil.parseXML;

/*
* Author：lankx
* 手动验证规则，实时性验证，第4条
* */

public class ReqAndDesignWcetConsistency {

    public static void excute() {
        Map<String, PathNode> pathNodeList = new HashMap<String, PathNode>();
        Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();

        parseXML("simulink0703.xml", componentListSimulink, channelListSimulink);
        parseXML("sysml0629.xml", componentListSysml, channelListSysml);

        if (componentListSysml.get("system") == null) {
            System.out.println("需求模型中没有找到可以验证的系统");
            return;
        }

        String reqSystemWcetString = componentListSysml.get("system").getAttr("delay");
        float reqSystemWcet = Float
                .valueOf(reqSystemWcetString.substring(0, reqSystemWcetString.length()-2));

        if (componentListSimulink.get("system") == null ||
            !componentListSimulink.get("system").getAttr("name").
                equalsIgnoreCase(componentListSysml.get("system").getAttr("name"))) {
            System.out.println("设计模型中没有找到与需求中对应的系统" + componentListSysml.get("system").getAttr("name"));
            return;
        }

        getSystemPathNode(componentListSimulink.get("system").getChannelList(),
                componentListSimulink,
                pathNodeList);

        if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
        for (String pathNodeKeyString : pathNodeList.keySet()) {
            System.out.println(pathNodeKeyString + " " +
                    pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " +
                    pathNodeList.get(pathNodeKeyString).getIsFirst() + " " +
                    pathNodeList.get(pathNodeKeyString).getWcet());
        }

        float targetWcet = getMaxPathWcet(pathNodeList);
        pathNodeList = new HashMap<>();

        if (reqSystemWcet < targetWcet) {
            System.out.println("系统" + componentListSimulink.get("system").getAttr("name") +
                    "在需求模型中的流延迟为" + reqSystemWcet +
                    "，在设计模型中最长的wcet为" + targetWcet +
                    "，不小于需求模型中对应的流延迟，不满足实时性规则\n");
        } else {
            System.out.println("系统" + componentListSimulink.get("system").getAttr("name") +
                    "在需求模型中的流延迟为" + reqSystemWcet +
                    "，在设计模型中最长的wcet为" + targetWcet +
                    "，小于需求模型中对应的流延迟，满足实时性规则\n");
        }

//      System.out.println("\naadl存储的结果为：");
//		for (String componentKey : componentListAadl.keySet()) {
//			System.out.println("\nComponent id : " + componentKey);
//			componentListAadl.get(componentKey).attrsToString();
//			for (String subComponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
//				System.out.println("\nsubComponent id : " + subComponentKey);
//				componentListAadl.get(componentKey).getSubComponentList().get(subComponentKey).attrsToString();
//			}
//		}

//        for (String componentKey : componentListSimulink.keySet()) {
////        	System.out.println(componentKey + " connect list size is " +
////        						componentListAadl.get(componentKey).getConnectionList().size());
//            if (componentListAadl.get(componentKey).getConnectionList().size() > 0) {
//                System.out.println("has connections");
//                String wcetString = componentListAadl.get(componentKey).getAttr("wcet");
//                float wcet = Float.valueOf(wcetString.substring(0, wcetString.length()-2));
//                //System.out.println("wcet is " + wcet);
//                getPathNode(componentListAadl.get(componentKey).getConnectionList(),
//                        componentListAadl.get(componentKey).getSubComponentList(),
//                        pathNodeList);
//
//                //float maxPathWcet = getMaxPathWcet(pathNodeList);
//                getMaxPathWcet(pathNodeList);
//                System.out.println("max path wcet is " + getMaxPathWcet(pathNodeList));
////        		if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
////        		for (String pathNodeKeyString : pathNodeList.keySet()) {
////        			System.out.println(pathNodeKeyString + " " +
////        					pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " +
////        					pathNodeList.get(pathNodeKeyString).getIsFirst() + " " +
////        					pathNodeList.get(pathNodeKeyString).getWcet());
////        		}
//            }
//        }
    }

    private static void getSystemPathNode(List<Channel> channelList,
                                          Map<String, Component> subComponentList,
                                          Map<String, PathNode> pathNodeList) {
//        System.out.println(channelList.size());
//        System.out.println(subComponentList.size());
        for (Channel channel : channelList) {
            String sourceId = channel.getAttr("source");
            //System.out.println("source is " + sourceId + " dest is " + channel.getAttr("dest"));
            for (String componentKey : subComponentList.keySet()) {
//                System.out.println("component " + subComponentList.get(componentKey).getAttr("id") +
//                        " linkpoint list size is " + subComponentList.get(componentKey).getLinkpointList().size());
                if (subComponentList.get(componentKey).getLinkpointList().size() > 0) {
                    for (Linkpoint linkpoint : subComponentList.get(componentKey).getLinkpointList()) {
                        if (linkpoint.getAttr("id").equals(sourceId)) {
                            sourceId = subComponentList.get(componentKey).getAttr("id");
//                            System.out.println("source id is " + sourceId + " " +
//                                    subComponentList.get(componentKey).getAttr("wcet"));
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
//                            System.out.println("dest id is " + destId + " " +
//                                    subComponentList.get(componentKey).getAttr("wcet"));
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
        //System.out.println(pathNode.getNextComponents().size());
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
