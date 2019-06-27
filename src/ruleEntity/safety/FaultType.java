package ruleEntity.safety;

import entity.*;
import utils.XMLParseUtil;

import java.util.*;

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
//        System.out.println(componentListSimulink.get("453762388").getStateList().get("23456").getAttr("faultType"));

        Set<String> componentSet = componentListSimulink.keySet();
        Iterator<String> iter = componentSet.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            componentList.add(str);
        }
        for (String componentId : componentList) {
            Map<String, State> stateList = componentListSimulink.get(componentId).getStateList();
            String componentName = componentListSimulink.get(componentId).getAttr("name");
            if(stateList!=null) {
                Set<String> stateSet = stateList.keySet();
//                System.out.println(stateSet);
                List<String> stateIds = new ArrayList<>();

                Iterator<String> it = stateSet.iterator();
                while (it.hasNext()) {
                    String str = it.next();
                    stateIds.add(str);
                }
                for(String stateId:stateIds){
                    String faultType=stateList.get(stateId).getAttr("faultType");
//                    System.out.println(faultType);
                    if(faultType!=null) {
                        List<String> exceptions = sysmlTypes(componentId);
                        if (!exceptions.contains(faultType)) {
                            System.out.println("component:" + componentName + "的故障类型" + faultType + "未在需求中定义");
                        }
                    }

                }
            }
        }
    }

    public static List<String> sysmlTypes(String componentIdSimulink){
        List<String> exceptionList=new ArrayList<>();
        Map<String, Component> componentListSysml = new HashMap<>();
        Map<String, Channel> channelListSysml = new HashMap<>();

        XMLParseUtil.parseXML("sysml(1).xml", componentListSysml, channelListSysml);

        List<Map<String,Map<String, String>>> list=mapping();
        for(Map<String,Map<String, String>> map:list){
            String simulinkId=map.get("simulink").get("id");
            if(simulinkId.equals(componentIdSimulink)) {
                String componentIDSysml = map.get("sysml").get("id");
                if(componentListSysml.get(componentIDSysml)!=null) {
                    Map<String, ExceptionXML> exceptions = componentListSysml.get(componentIDSysml).getExceptionList();
                    List<String> exceptionIdList = getExceptionIdList(exceptions);
                    for (String exceptionId : exceptionIdList) {
                        exceptionList.add(exceptions.get(exceptionId).getAttr("name"));
                    }
                }
            }
        }
        System.out.println(exceptionList);
        return exceptionList;
    }

    public static List<String> getExceptionIdList(Map<String, ExceptionXML> exceptions) {
        List<String> exceptionList=new ArrayList<>();
        Set<String> exceptionSet = exceptions.keySet();

        Iterator<String> iter = exceptionSet.iterator();

        while (iter.hasNext()) {
            String str = iter.next();
            exceptionList.add(str);
        }
//        System.out.println(componentList);

        return exceptionList;
    }
    /**
     * 模型间的映射表
     * @return
     */
    public static List<Map<String,Map<String, String>>> mapping() {
        List<Map<String,Map<String, String>>> list=new ArrayList<>();

        Map<String,String> idSysml=new LinkedHashMap<>();
        idSysml.put("id","966564649");

        Map<String,String> idAADL=new LinkedHashMap<>();
        idAADL.put("id","1099650349");

        Map<String,String> idSimulink=new LinkedHashMap<>();
        idSimulink.put("id","453762388");

        Map<String,String> idSysml_1=new LinkedHashMap<>();
        idSysml_1.put("id","-587841842");

        Map<String,String> idAADL_1=new LinkedHashMap<>();
        idAADL_1.put("id","-99369205");

        Map<String,String> idSimulink_1=new LinkedHashMap<>();
        idSimulink_1.put("id","453762388");

        Map<String,Map<String, String>> mapModel=new LinkedHashMap<>();
        mapModel.put("sysml",idSysml);
        mapModel.put("aadl",idAADL);
        mapModel.put("simulink",idSimulink);

        Map<String,Map<String, String>> mapModel1=new LinkedHashMap<>();
        mapModel1.put("sysml",idSysml_1);
        mapModel1.put("aadl",idAADL_1);
        mapModel1.put("simulink",idSimulink_1);

        list.add(mapModel);
        list.add(mapModel1);
//        System.out.println(list);
        return list;
    }


    public static void main(String[] args){
//        mapping();
        excute();
    }
}
