package ruleEntity.safety;

import entity.*;
import java.util.*;
import static utils.XMLParseUtil.parseXML;

public class PropagationDeal {
    /**
     * 故障传播的定义是否完整，是否被处理
     */
    public static void excute() {
        StringBuilder sb=new StringBuilder();
        Map<String, Component> componentListAADL = new LinkedHashMap<>();

        List<String> componentList = new ArrayList<>();
        componentList = getComponentIdList();

        Map<String, Channel> channelListAADL = new LinkedHashMap<>();

        parseXML("aadl(5).xml", componentListAADL, channelListAADL);

        for (String componentId : componentList) {
            Map<String, Propagation> propagationList = componentListAADL.get(componentId)
                    .getPropagationList();
            String componentName = componentListAADL.get(componentId).getAttr("name");
            List<String> propagationIds = getpropagationIdList(propagationList);
            for (String propagationId : propagationIds) {
                String direction = propagationList.get(propagationId).getAttr("direction");
                String fault = propagationList.get(propagationId).getAttr("fault");
                if (("out").equals(direction)) {
                    String destPort = propagationList.get(propagationId).getAttr("port_id");
                    boolean prop = false;
                    for (String compoId : componentList) {
                        String compoName = componentListAADL.get(compoId).getAttr("name");
                        List<Linkpoint> linkpointList = componentListAADL.get(compoId).getLinkpointList();
                        for (Linkpoint linkpoint : linkpointList) {
                            if (linkpoint.getAttr("name").equals(destPort)) {
                                Map<String, Propagation> propagations = componentListAADL.get(compoId)
                                        .getPropagationList();
                                List<String> propagationIdList = getpropagationIdList(propagations);

                                for (String propoga : propagationIdList) {
                                    if (propagationList.get(propagationId).getAttr("direction").equals("in")) {
                                        if (propagationList.get(propagationId).getAttr("fault").equals(fault)) {
                                            prop = true;
                                        }
                                    }
                                }
                                if(!prop){
                                    sb.append("component:"+componentName+"的故障传播到"+"component:"+compoName+"没有被处理"+"\n");
                                    System.out.println(sb.toString()+"\n");
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
        parseXML("aadl(5).xml", componentListAADL, channelListAADL);
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
