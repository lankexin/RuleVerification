package ruleEntity.safety;

import entity.Channel;
import entity.Component;
import entity.State;
import entity.Transition;

import java.util.*;

import static entity.State.attrsList;
import static utils.XMLParseUtil.parseXML;

public class FaultType {
    /**
     * 查找每一个故障状态，判定其Type是否属于需求中定义的
     */
    public static void excute() {
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        List<String> componentList = new ArrayList<>();

        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();

        parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);

        Set<String> componentSet = componentListSimulink.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            componentList.add(str);
        }
        for (String componentId : componentList) {
            Map<String, State> stateList = componentListSimulink.get(componentId).getStateList();
            System.out.println();
            if(stateList!=null) {
                Set<String> stateSet = stateList.keySet();
                System.out.println(stateSet);
                List<String> stateIds = new ArrayList<>();

                Iterator<String> it = stateSet.iterator();
                while (it.hasNext()) {
                    String str = it.next();
                    stateIds.add(str);
                }
                for(String stateId:stateIds){
                    System.out.println(stateId);

                    String faultType=stateList.get(stateId).getAttr("faultType");
                    System.out.println(faultType);
                }
            }
        }
    }
    public static void main(String[] args){
        excute();

    }
}
