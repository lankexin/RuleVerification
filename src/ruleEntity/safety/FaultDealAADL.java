package ruleEntity.safety;

import entity.Channel;
import entity.Component;
import entity.State;
import entity.Transition;

import java.util.*;

import static entity.State.attrsList;
import static utils.XMLParseUtil.parseXML;

public class FaultDealAADL {
    public static void excute(){
        Map<String, Component> componentListSimulink = new HashMap<>();
        List<String> componentList = new ArrayList<>();

        Map<String, Channel> channelListSimulink = new HashMap<>();

        parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);


        Set<String> componentSet = componentListSimulink.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            componentList.add(str);
        }
        System.out.println(componentList);

        for (String componentId : componentList) {
            System.out.println(attrsList());
            List<Transition> transitionList = componentListSimulink.get(componentId).getTransitionList();
            Map<String, State> stateList = componentListSimulink.get(componentId).getStateList();

            System.out.println("----" + stateList);

            String componentName = componentListSimulink.get(componentId).getAttr("name");
            if (transitionList != null) {
                for (Transition transition : transitionList) {
                    String sourceState = transition.getAttr("source");
                    String destState = transition.getAttr("dest");
                    System.out.println(stateList.get(destState));
                    if (attrsList().contains("faultState") &&
                            "true".equals(stateList.get(destState).getAttr("faultState")))
                        if (attrsList().contains("exit") && "report fault message".equals(stateList.get(destState).getAttr("exit"))) {
                            System.out.println("component:" + componentName + "记录故障信息处理故障:" +
                                    stateList.get(destState).getAttr("name"));
                        }
                }
            }
        }
    }
    public static void main(String[] args){
        excute();
    }
}
