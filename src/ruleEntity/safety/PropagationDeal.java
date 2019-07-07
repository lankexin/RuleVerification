package ruleEntity.safety;

import entity.*;

import java.util.*;

import static ruleEntity.safety.FaultDealAADL.process;
import static utils.KeySet.keySet;
import static utils.XMLParseUtil.parseXML;

public class PropagationDeal {
    /**
     * 故障传播的定义是否完整，是否被处理
     */
    public static void excute() {
        StringBuilder sb = new StringBuilder();

        Map<String, Component> componentListAADL = new LinkedHashMap<>();
        Map<String, Channel> channelListAADL = new LinkedHashMap<>();
        parseXML("aadl(6).xml", componentListAADL, channelListAADL);

        List<String> componentList = keySet(componentListAADL);

        List<String> channelList = keySet(channelListAADL);

        for (String componentId : componentList) {
            Map<String, Propagation> propagationList = componentListAADL.get(componentId).getPropagationList();
            String componentName = componentListAADL.get(componentId).getAttr("name");
            List<String> propagationIds = keySet(propagationList);
            for (String propagationId : propagationIds) {
                String direction = "";
                String fault = propagationList.get(propagationId).getAttr("fault");
                List<Linkpoint> linkpointList_1 = componentListAADL.get(componentId).getLinkpointList();

                String destPort = propagationList.get(propagationId).getAttr("port_id");
                for (Linkpoint linkpoint : linkpointList_1) {
                    if (linkpoint.getAttr("id").equals(destPort))
                        direction = linkpoint.getAttr("direction");
                }
//                System.out.println(direction);
                if (("out").equals(direction)) {
                    boolean prop = false;
                    String destId = "";
                    for (String channel : channelList) {
                        String sourceId = channelListAADL.get(channel).getAttr("source");
                        if (sourceId.equals(destPort)) {
                            destId = channelListAADL.get(channel).getAttr("dest");
                        }
                    }
//                    System.out.println(destId);
                    if (!destId.isEmpty()) {
                        for (String compoId : componentList) {
                            String compoName = componentListAADL.get(compoId).getAttr("name");
                            List<Linkpoint> linkpointList = componentListAADL.get(compoId).getLinkpointList();
                            for (Linkpoint linkpoint : linkpointList) {
                                if (linkpoint.getAttr("id").equals(destId)) {
                                    Map<String, Propagation> propagations = componentListAADL.get(compoId).getPropagationList();
                                    List<String> propagationIdList = keySet(propagations);
                                    for (String propoga : propagationIdList) {
                                        String portId = propagations.get(propoga).getAttr("port_id");
                                        String faultType = propagations.get(propoga).getAttr("fault");
                                        if (portId.equals(destId) && faultType.equals(fault)) {
//                                            System.out.println("----");
                                            prop = true;
                                            List<Transition> transitionList = componentListAADL.get(compoId)
                                                    .getTransitionList();
                                            Map<String, State> stateList = componentListAADL.get(compoId).getStateList();
                                            String componentName_1 = componentListAADL.get(compoId).getAttr("name");
                                            if (transitionList != null) {
                                                for (Transition transition : transitionList) {
                                                    String dest = transition.getAttr("dest");
                                                    if (stateList.get(dest).getAttr("name").equals("FailStop")) {
                                                        String log = process(stateList, componentName_1, dest, transitionList);
                                                        System.out.println("component:" + componentName + "的故障传播到" +
                                                                "component:" + compoName);
                                                        System.out.println(log);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!prop) {
                                        sb.append("component:" + componentName + "的故障传播到" + "component:" + compoName +
                                                "没有被处理" + "\n");
                                        System.out.println(sb.toString() + "\n");
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    public static List<String> getComponentIdList() {
        Map<String, Component> componentListAADL = new LinkedHashMap<>();
        List<String> componentList = new ArrayList<>();
        Map<String, Channel> channelListAADL = new LinkedHashMap<>();
        parseXML("aadl(9).xml", componentListAADL, channelListAADL);
        Set<String> componentSet = componentListAADL.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            componentList.add(str);
        }
//        System.out.println(componentList);

        return componentList;
    }

    public static List<String> getpropagationIdList(Map<String, Propagation> propagationList) {
        List<String> propagations = new ArrayList<>();
        Set<String> propagationSet = propagationList.keySet();
        Iterator<String> iter = propagationSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            propagations.add(str);
        }
//        System.out.println(propagations);
        return propagations;
    }

    public static void main(String[] args) {
        excute();
    }
}
