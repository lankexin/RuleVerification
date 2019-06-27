package ruleEntity.base;

import entity.Channel;
import entity.Component;
import entity.Linkpoint;
import utils.XMLParseUtil;

import java.util.*;

import static ruleEntity.safety.FaultType.mapping;
import static utils.KeySet.keySet;

public class ComponentInOut {
    /**
     * 需求中某个组件的输入输出接口，一定与子系统设计模型中的某个输入输出接口对应
     */
    public static void excute() {
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();

        Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();

        XMLParseUtil.parseXML("simulink(2).xml", componentListSimulink,channelListSimulink);
        XMLParseUtil.parseXML("sysml(4).xml", componentListSysml, channelListSysml);

        List<String> componentSysmlList = new ArrayList<>();
        componentSysmlList = keySet(componentListSysml);

        for(String sysmlCom:componentSysmlList){
            String componentName = componentListSysml.get(sysmlCom).getAttr("name");
            List<Map<String,Map<String, String>>> list=mapping();
            String simulinkId="";
            for(Map<String,Map<String, String>> map:list){
//                System.out.println(map);
                if(map.get("sysml")!=null && map.get("sysml").get("id")!=null &&
                        map.get("sysml").get("id").equals(sysmlCom)){
//                    System.out.println("---"+map.get("sysml").get("id").equals(sysmlCom));
                    simulinkId=map.get("simulink").get("id");
                }
            }
//            System.out.println("simulinkId"+simulinkId);
            if(!simulinkId.isEmpty()){
                List<Linkpoint> linkpointList = componentListSysml.get(sysmlCom).getLinkpointList();
                List<String> linkpointNameList = new LinkedList<>();
                for (Linkpoint linkpoint : linkpointList) {
                    linkpointNameList.add(linkpoint.getAttr("name"));
                }
//                System.out.println(linkpointNameList);

                List<Linkpoint> linkpointList_simulink = componentListSimulink.get(simulinkId).getLinkpointList();
                List<String> linkpointNameList_simulink = new LinkedList<>();
                for (Linkpoint linkpoint : linkpointList_simulink) {
                    linkpointNameList_simulink.add(linkpoint.getAttr("name"));
                }
//                System.out.println(linkpointNameList_simulink);

                for(String link:linkpointNameList){
                    if(!linkpointNameList_simulink.contains(link))
                        System.out.println("Sysml模型中，组件"+componentName+"的端口"+link+"没有对应的子系统的端口");
                }
            }
            else
                System.out.println("Sysml模型中的组件"+componentName+"没有子系统的组件与之对应");
        }
    }

    public static void main(String[] args) {
        excute();
    }

}
