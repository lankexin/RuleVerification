package ruleEntity.safety;

import entity.*;
import utils.XMLParseUtil;

import java.util.*;

import static utils.Dataconnect.connect;
import static utils.KeySet.keySet;
import static utils.XMLParseUtil.parseXML;

public class FaultType {
    /**
     * 查找每一个故障状态，判定其Type是否属于需求中定义的
     */
    public static void excute() {
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();

        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();

        parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);
//        System.out.println(componentListSimulink.get("453762388").getStateList().get("23456").getAttr("faultType"));

        List<String> componentList =keySet(componentListSimulink);
        for (String componentId : componentList) {
            Map<String, State> stateList = componentListSimulink.get(componentId).getStateList();
            String componentName = componentListSimulink.get(componentId).getAttr("name");
            if(stateList!=null) {
                List<String> stateIds = keySet(stateList);

                for(String stateId:stateIds){
                    String faultType=stateList.get(stateId).getAttr("faultType");


                    if(faultType!=null) {
                        List<String> exceptions = sysmlTypes(componentId);
                        if (!exceptions.contains(faultType)) {
                            System.out.println("component:" + componentName + "的故障类型" + faultType + "未在需求中定义");
                        }
                    }


                    List<State> subStateList=stateList.get(stateId).getSubStateList();
                    for(State subState:subStateList){
                        String subFaultType=subState.getAttr("faultType");
                        if(subFaultType!=null){
                            List<String> exceptionList = sysmlTypes(componentId);
                            if (!exceptionList.contains(subFaultType)) {
                                System.out.println("component:" + componentName + "的故障类型" + subFaultType + "未在需求中定义");
                            }
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

        XMLParseUtil.parseXML("sysml(4).xml", componentListSysml, channelListSysml);

        List<Map<String,Map<String, String>>> list=mapping();

/**
 for(Map<String,Map<String, String>> map:list){
 String simulinkId=map.get("simulink").get("id");
 if(simulinkId.equals(componentIdSimulink)) {
 String componentIDSysml = map.get("sysml").get("id");
 if(componentListSysml.get(componentIDSysml)!=null) {
 Map<String, ExceptionXML> exceptions = componentListSysml.get(componentIDSysml)
 .getExceptionList();
 List<String> exceptionIdList = getExceptionIdList(exceptions);
 for (String exceptionId : exceptionIdList) {
 exceptionList.add(exceptions.get(exceptionId).getAttr("name"));
 }
 }
 }
 }
 */
        String sql="select * from mapping where simulink_id="+componentIdSimulink;
//        System.out.println(componentIdSimulink);
//        System.out.println(sql);
        String sysmlId=connect(sql,"sysml");
//        System.out.println(sysmlId);
        if(sysmlId!=null){
//            System.out.println(sysmlId);
            Map<String, ExceptionXML> exceptions = componentListSysml.get(sysmlId).getExceptionList();
            List<String> exceptionIdList = getExceptionIdList(exceptions);
            for (String exceptionId : exceptionIdList) {
                exceptionList.add(exceptions.get(exceptionId).getAttr("name"));
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
