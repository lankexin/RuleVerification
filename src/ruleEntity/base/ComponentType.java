package ruleEntity.base;

import entity.Channel;
import entity.Component;
import utils.XMLParseUtil;
import java.util.*;
import static ruleEntity.safety.FaultType.mapping;
import static utils.Dataconnect.connect;
import static utils.KeySet.keySet;

public class ComponentType {
    /**
     * sysml的组件类型和simulink的组件类型一致
     */
    public static void excute(){
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();

        Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();

        XMLParseUtil.parseXML("simulink(2).xml", componentListSimulink,channelListSimulink);
        XMLParseUtil.parseXML("sysml(4).xml", componentListSysml, channelListSysml);

        List<String> componentSysmlList = keySet(componentListSysml);
//        System.out.println(componentSysmlList);

        for(String sysmlCom:componentSysmlList) {
            if(sysmlCom.equals("system"))
                continue;
            String componentName = componentListSysml.get(sysmlCom).getAttr("name");

            /*List<Map<String,Map<String, String>>> list=mapping();
            String simulinkId="";
            for(Map<String,Map<String, String>> map:list){
                if(map.get("sysml")!=null && map.get("sysml").get("id")!=null &&
                        map.get("sysml").get("id").equals(sysmlCom)){
                    simulinkId=map.get("simulink").get("id");
                }
            }*/

            String simulinkId="";

            String sql="select * from mapping where sysml_id="+sysmlCom;
//        System.out.println(sql);
            simulinkId=connect(sql,"simulink");

            if(simulinkId!=null && !simulinkId.isEmpty()){
                String componentSysmlType = componentListSysml.get(sysmlCom).getAttr("type");
                String componentSimulinkType=componentListSimulink.get(simulinkId).getAttr("type");

                if(!componentSysmlType.equals(componentSimulinkType))
                    System.out.println("Sysml模型中的组件"+componentName+"的组件类型与子系统设计模型中的组件不一致");
            }
            else
                System.out.println("Sysml模型中的组件"+componentName+"没有子系统的组件与之对应");
        }
    }

    public static void main(String[] args) {
        excute();
    }
}
