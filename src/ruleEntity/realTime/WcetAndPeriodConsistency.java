package ruleEntity.realTime;

import entity.*;

import java.util.*;

import static utils.XMLParseUtil.parseXML;

/*
* Author：lankx
* 手动验证规则文档，实时性规则，第三条
* 对aadl模型进行验证
* */

public class WcetAndPeriodConsistency {
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


        for (String componentKey : componentListAadl.keySet()) {
            Component currentComponent = componentListAadl.get(componentKey);
            //currentComponent.attrsToString();
//            System.out.println(componentKey + " connect list size is " +
//                    currentComponent.getConnectionList().size() + " linkpoint size is " +
//                    currentComponent.getLinkpointList().size());

            if (currentComponent.getConnectionList().isEmpty() || currentComponent.getLinkpointList().isEmpty()) continue;

            for (Connection connection : currentComponent.getConnectionList()) {
                Linkpoint destLinkpoint = null;
                String destId = connection.getAttr("dest");
                String sourceId = connection.getAttr("source");

                for (Linkpoint linkpoint : currentComponent.getLinkpointList()) {
                    if (linkpoint.getAttr("id").equals(destId)) {
                        destLinkpoint = linkpoint;
                        break;
                    }
                }

                if (destLinkpoint != null && destLinkpoint.getAttr("direction").equals("out")) {
                    boolean foundSourceComponent = false;
                    for (String subComponentKey : currentComponent.getSubComponentList().keySet()) {
                        if (currentComponent.getSubComponentList().get(subComponentKey).getPortList().isEmpty()) continue;
                        for (Port port : currentComponent.getSubComponentList().get(subComponentKey).getPortList()) {
                            if (port.getAttr("id").equals(sourceId)) {
                                sourceId = subComponentKey;
                                foundSourceComponent = true;
                                break;
                            }
                        }
                        if (foundSourceComponent) break;
                    }

                    getPathNode(currentComponent.getConnectionList(),
                            currentComponent.getSubComponentList(),
                            pathNodeList);

                    float targetWcet = getSpecificPathWcet(pathNodeList, sourceId);

//                    System.out.println("Linkpoint " + destLinkpoint.getAttr("id") + " " + destLinkpoint.getAttr("name") +
//                            " period is " + destLinkpoint.getAttr("period") +
//                            " , path wcet is " + targetWcet);
                    if (targetWcet > Float.valueOf(destLinkpoint.getAttr("period").substring(0, destLinkpoint.getAttr("period").length()-2))) {
                        System.out.println("数据" + destLinkpoint.getAttr("id") + " " + destLinkpoint.getAttr("name") +
                                "周期为" + destLinkpoint.getAttr("period") +
                                " , 生成该数据的子组件路径执行时间为" + targetWcet + "，大于该数据生成的周期，不满足实时性规则");
                    } else if (targetWcet == 0) {
                        System.out.println("数据" + destLinkpoint.getAttr("id") + " " + destLinkpoint.getAttr("name") +
                                "无法验证该条规则，因为其关联的路径有循环");
                    } else {
                        System.out.println("数据" + destLinkpoint.getAttr("id") + " " + destLinkpoint.getAttr("name") +
                                "周期为" + destLinkpoint.getAttr("period") +
                                " , 生成该数据的子组件路径执行时间为" + targetWcet + "，不大于该数据生成的周期，满足实时性规则");
                    }

                }
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

    private static float getSpecificPathWcet(Map<String, PathNode> pathNodeList,
                                             String targetLastId) {
        Stack<String> pathNodeIdStack = new Stack<String>();
        float maxPathWcet = 0;
        for (String pathNodeKeyString : pathNodeList.keySet()) {
            if (pathNodeList.get(pathNodeKeyString).getIsFirst()) {
                float maxPathWcetTemp = 0;
                float currentWcet = pathNodeList.get(pathNodeKeyString).getWcet();
                pathNodeIdStack.add(pathNodeList.get(pathNodeKeyString).getId());
                maxPathWcetTemp = calculateSpecificWcet(maxPathWcetTemp, pathNodeList.get(pathNodeKeyString),
                        currentWcet, pathNodeIdStack, targetLastId);
                //System.out.println(maxPathWcet + "max path wcet temp is " + maxPathWcetTemp);
                if (maxPathWcet < maxPathWcetTemp) maxPathWcet = maxPathWcetTemp;
                pathNodeIdStack.clear();
            }
        }

        return maxPathWcet;
    }

    private static float calculateSpecificWcet(float maxPathWcet,
                                          PathNode pathNode,
                                          float currentWcet,
                                          Stack<String> pathNodeIdStack,
                                          String targetLastId) {
        if (pathNode.getNextComponents().size() <= 0) {
            if (pathNode.getId().equals(targetLastId)) maxPathWcet = currentWcet;
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
            maxPathWcet = calculateSpecificWcet(maxPathWcet, nextPathNode, currentWcet, pathNodeIdStack, targetLastId);
            currentWcet -= nextPathNode.getWcet();
            pathNodeIdStack.pop();
        }
        return maxPathWcet;
    }

    public static void main(String[] args) {
        excute();
    }
}
