package ruleEntity.safety;

import entity.Channel;
import entity.Component;
import entity.State;
import entity.Transition;

import java.util.*;

//import static entity.State.attrsList;
import static utils.XMLParseUtil.parseXML;

public class FaultDealSimulink {
    /**
     * 子系统的故障处理验证，发生故障的状态是否恢复到了故障状态或者退出故障状态并记录故障信息
     */
    public static void excute() {
//        List<HashMap<String,String>> list=new ArrayList<>();
//        //list=XMLParseUtil.parseXML("FlightSystem.xml");
//
//        for(HashMap<String,String> hash:list){
//            String tmpClass=hash.get("class");
//            if(tmpClass.equals("component")){
//                String componentName=hash.get("name");
//                String safetyLevel=hash.get("safetyLevel");
//                String stage=hash.get("stage");
//
//                hash.put("class","checkedComponent");
//
//                for(HashMap<String,String> hashNew:list){
//
//                    if(componentName.equals(hashNew.get("name"))&&(!hashNew.get("class").equals("checkedComponent"))) {
//                        if(!safetyLevel.equals(hashNew.get("safetyLevel"))){
//                            System.out.println("组件"+componentName+"需求和设计间的安全级别不一致");
//                            System.out.print(hashNew);
//                        }
//                    }
//                }
//            }
//        }
        Map<String, Component> componentListSimulink = new HashMap<>();
        List<String> componentList = new ArrayList<>();

        Map<String, Channel> channelListSimulink = new HashMap<>();

        parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);

        System.out.println(channelListSimulink);
        System.out.println(componentListSimulink);
        System.out.println(componentListSimulink.get("453762388").getStateList());
        Set<String> componentSet = componentListSimulink.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            componentList.add(str);
        }
        System.out.println(componentList);

        for (String componentId : componentList) {
//            System.out.println(attrsList());
            List<Transition> transitionList = componentListSimulink.get(componentId).getTransitionList();
            Map<String, State> stateList = componentListSimulink.get(componentId).getStateList();

            System.out.println("----" + stateList);
            String componentName = componentListSimulink.get(componentId).getAttr("name");
            if (transitionList != null) {
                for (Transition transition : transitionList) {
                    String sourceState = transition.getAttr("source");
                    String destState = transition.getAttr("dest");
                    System.out.println(stateList.get(destState));
//                    if (attrsList().contains("faultState") &&
//                            "true".equals(stateList.get(destState).getAttr("faultState")))
//                        if (attrsList().contains("exit") && "report fault message".equals(stateList.get(destState).getAttr("exit"))) {
//                            System.out.println("component:" + componentName + "记录故障信息处理故障:" +
//                                    stateList.get(destState).getAttr("name"));
//                        }
                }
            }
        }
    }
    public static void main(String[] args) {
        excute();
    }
}
