package ruleEntity.realTime;

import entity.Channel;
import entity.Component;
import entity.State;
import entity.Transition;

import java.util.*;

import static utils.XMLParseUtil.parseXML;

/*
* Author：lankx
* 手动验证文档，实时性规则，第2条
* */


public class StateWcetComsistency {

    public static void excute() {
        Map<String, PathNode> pathNodeList = new HashMap<String, PathNode>();
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();

        parseXML("simulink0703.xml", componentListSimulink, channelListSimulink);
//      System.out.println("\naadl存储的结果为：");
//		for (String componentKey : componentListAadl.keySet()) {
//			System.out.println("\nComponent id : " + componentKey);
//			componentListAadl.get(componentKey).attrsToString();
//			for (String subComponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
//				System.out.println("\nsubComponent id : " + subComponentKey);
//				componentListAadl.get(componentKey).getSubComponentList().get(subComponentKey).attrsToString();
//			}
//		}

        for (String componentKey : componentListSimulink.keySet()) {
            Component currentComponent = componentListSimulink.get(componentKey);
//        	System.out.println(componentKey + " connect list size is " +
//        						componentListAadl.get(componentKey).getConnectionList().size());
            if (componentListSimulink.get(componentKey).getTransitionList().size() > 0) {
                //System.out.println("has connections");
                if (componentListSimulink.get(componentKey).getAttr("wcet") != null) {
                    String wcetString = componentListSimulink.get(componentKey).getAttr("wcet");
                    float wcet = Float.valueOf(wcetString.substring(0, wcetString.length() - 2));

                    getPathNode(componentListSimulink.get(componentKey).getTransitionList(),
                            componentListSimulink.get(componentKey).getStateList(),
                            pathNodeList);

//                    if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
//                    for (String pathNodeKeyString : pathNodeList.keySet()) {
//                        System.out.println(pathNodeKeyString + " " +
//                                pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " +
//                                pathNodeList.get(pathNodeKeyString).getIsFirst() + " " +
//                                pathNodeList.get(pathNodeKeyString).getWcet());
//                    }

                    //float maxPathWcet = getMaxPathWcet(pathNodeList);
                    float targetWcet = getMaxPathWcet(pathNodeList);
                    pathNodeList = new HashMap<>();
                    //System.out.println("max path wcet is " + getMaxPathWcet(pathNodeList));

                    if (wcet < targetWcet) {
                        System.out.println("子系统" + componentListSimulink.get(componentKey).getAttr("name") +
                                "的wcet为" + wcet + "，其状态最长无循环路径的wcet为" + targetWcet +
                                "，大于该子系统的wcet，不满足实时性规则\n");
                    } else {
                        System.out.println("子系统" + componentListSimulink.get(componentKey).getAttr("name") +
                                "的wcet为" + wcet + "，其状态最长无循环路径的wcet为" + targetWcet +
                                "，不大于该子系统的wcet，满足实时性规则\n");
                    }
                }
            }

            if (!currentComponent.getStateList().isEmpty()) {
                for (String stateKey : currentComponent.getStateList().keySet()) {
                    State currentState = currentComponent.getStateList().get(stateKey);
                    if (!currentState.getTransitionList().isEmpty()) {
                        //System.out.println("has connections");
                        String stateWcetString = currentState.getAttr("wcet");
                        float stateWcet = Float.valueOf(stateWcetString.substring(0, stateWcetString.length()-2));

                        Map<String, State> subStateMap = new HashMap<>();
                        for (State state : currentState.getSubStateList()) {
                            subStateMap.put(state.getAttr("id"), state);
                        }
                        getPathNode(currentState.getTransitionList(),
                                subStateMap,
                                pathNodeList);

//                        if (pathNodeList.size() != 0) System.out.println(pathNodeList.size() + "\n");
//                        for (String pathNodeKeyString : pathNodeList.keySet()) {
//                            System.out.println(pathNodeKeyString + " " +
//                                    pathNodeList.get(pathNodeKeyString).getNextComponents().size() + "  " +
//                                    pathNodeList.get(pathNodeKeyString).getIsFirst() + " " +
//                                    pathNodeList.get(pathNodeKeyString).getWcet());
//                        }

                        float targetWcet = getMaxPathWcet(pathNodeList);
                        if (stateWcet < targetWcet) {
                            System.out.println("状态" + currentState.getAttr("name") +
                                    "的wcet为" + stateWcet + "，其子状态最长无循环路径的wcet为" + targetWcet+
                                    "，大于该状态的wcet，不满足实时性规则\n");
                        } else {
                            System.out.println("状态" + currentState.getAttr("name") +
                                    "的wcet为" + stateWcet + "，其子状态最长无循环路径的wcet为" + targetWcet +
                                    "，不大于该状态的wcet，满足实时性规则\n");
                        }
                    }
                    pathNodeList = new HashMap<>();
                }
            }
        }
    }

    private static void getPathNode(List<Transition> transitionList,
                                    Map<String, State> stateList,
                                    Map<String, PathNode> pathNodeList) {
        for (Transition transition : transitionList) {
            String sourceId = transition.getAttr("source");
            String destId = transition.getAttr("dest");
            if (stateList.get(destId) == null || stateList.get(sourceId) == null) continue;
            if (pathNodeList.get(destId) != null) pathNodeList.get(destId).setIsFirst(false);
            else {
                PathNode newDestNode = new PathNode(destId, stateList.get(destId).getAttr("wcet"),
                        stateList.get(destId).getAttr("name"), false);
                pathNodeList.put(destId, newDestNode);
            }
            if (pathNodeList.get(sourceId) != null) {
                pathNodeList.get(sourceId).getNextComponents().add(pathNodeList.get(destId));
            } else {
                PathNode newSourceNode = new PathNode(sourceId, stateList.get(sourceId).getAttr("wcet"),
                        stateList.get(sourceId).getAttr("name"), true);
                newSourceNode.getNextComponents().add(pathNodeList.get(destId));
                pathNodeList.put(sourceId, newSourceNode);
            }

        }
    }

    private static float getMaxPathWcet(Map<String, PathNode> pathNodeList) {
        //System.out.println(pathNodeList.size());
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


    /*
    * 该函数用于计算pathNode为起点的最长的wcet
    * maxPathWcet: 计算得到的执行时间最长路径的wcet
    * pathNode: 从该节点开始计算之后的路径长度
    * currentWcet: 到达当前节点的最长的wcet
    * pathNodeStack: 包括当前节点的路径中所有节点的id
    * */
    private static float calculateMaxWcet(float maxPathWcet,
                                          PathNode pathNode,
                                          float currentWcet,
                                          Stack<String> pathNodeIdStack) {
        //ystem.out.println("path node id is " + pathNode.getId() + " " + pathNode.getName() + " " + pathNode.getNextComponents().size());
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
                    if (pathNode.getName().equalsIgnoreCase("idle") &&
                        currentWcet > maxPathWcet) {
                        maxPathWcet = currentWcet;
                    }
                    pathNodeIdStack.add(pathNode.getId());
                    return maxPathWcet;
                }
            }
            pathNodeIdStack.add(pathNode.getId());
        }
        for (PathNode nextPathNode : pathNode.getNextComponents()) {
            currentWcet += nextPathNode.getWcet();
            pathNodeIdStack.add(nextPathNode.getId());
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
