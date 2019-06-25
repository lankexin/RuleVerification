package ruleEntity.safety;

import entity.Channel;
import entity.Component;
import entity.State;
import entity.Transition;

import java.util.*;
import static utils.XMLParseUtil.parseXML;

public class FaultDealAADL {
    public static void excute() {
        Map<String, Component> componentListAADL = new HashMap<>();
        List<String> componentList = new ArrayList<>();

        Map<String, Channel> channelListAADL = new HashMap<>();

        parseXML("aadl(1).xml", componentListAADL, channelListAADL);

        Set<String> componentSet = componentListAADL.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            componentList.add(str);
        }
//        System.out.println(componentList);

        for (String componentId : componentList) {
            List<Transition> transitionList = componentListAADL.get(componentId).getTransitionList();
            Map<String, State> stateList = componentListAADL.get(componentId).getStateList();

            String componentName = componentListAADL.get(componentId).getAttr("name");
            if (transitionList != null) {
                for (Transition transition : transitionList) {
                    String sourceState = transition.getAttr("source");
                    String destState = transition.getAttr("dest");
//                    System.out.println(stateList.get(destState));
                    if (destState.equals("FailStop")) {
                        String log = process(componentName, destState, transitionList);
                        System.out.println(log);
                    }
                }
            }
        }
    }

    public static String process(String componentName, String faultState, List<Transition> transitionList) {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (Transition transition : transitionList) {
            if (transition.getAttr("source").equals("FailStop") && transition.getAttr("dest")
                                                                                     .equals("Operational")) {
                sb.append("component:" + componentName + "发生故障FailStop" + "恢复为正常状态处理故障");
                flag = true;
            }
        }
        if (!flag)
            sb.append("component:" + componentName + "发生故障FailStop" + ",故障未被处理");
        return sb.toString();
    }

    public static void main(String[] args) {
        excute();
    }
}
