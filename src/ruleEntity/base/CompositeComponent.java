package ruleEntity.base;

import entity.*;
import utils.XMLParseUtil;

import java.util.*;

import static utils.KeySet.keySet;

public class CompositeComponent {
    /**
     * 复合组件的端口和子组件端口的一致性对应
     */
    public static void excute() {
        Map<String, Component> componentListAadl = new LinkedHashMap<>();
        Map<String, Channel> channelListAadl = new LinkedHashMap<>();

        Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();

        XMLParseUtil.parseXML("aadl(9).xml", componentListAadl, channelListAadl);
        XMLParseUtil.parseXML("sysml(4).xml", componentListSysml, channelListSysml);

        List<String> componentAADLList = keySet(componentListAadl);

        List<String> componentSysmlList =  keySet(componentListSysml);
//        System.out.println(componentList);

        for (String componentId : componentAADLList) {
            Map<String, Component> sub_1 = componentListAadl.get(componentId).getSubComponentList();
            String componentName = componentListAadl.get(componentId).getAttr("name");
//            System.out.println(componentName);
            List<Linkpoint> linkpointList = componentListAadl.get(componentId).getLinkpointList();
            List<String> linkpointNameList = new LinkedList<>();
            for (Linkpoint linkpoint : linkpointList) {
                linkpointNameList.add(linkpoint.getAttr("name"));
            }
            if (sub_1 != null && !sub_1.isEmpty()) {
                List<String> subList = keySet(sub_1);
                List<String> portNameList=new LinkedList<>();
                for (String sub : subList) {
                    if (sub_1.get(sub).getAttr("type").equals("task")) {
                        List<Port> portList = sub_1.get(sub).getPortList();
                        for (Port port : portList) {
                            portNameList.add(port.getAttr("name"));
                        }
                    }
                    if (sub_1.get(sub).getAttr("type").equals("partition")) {
                        Map<String, Component> sub_2 = sub_1.get(sub).getSubComponentList();
                        if (sub_2 != null) {
                            List<String> subtaskList = keySet(sub_2);
                            String taskName = "";
                            for (String task : subtaskList) {
                                taskName = sub_2.get(task).getAttr("Name");
                                List<Port> portList = sub_2.get(task).getPortList();
                                for (Port port : portList) {
                                    portNameList.add(port.getAttr("name"));
                                }
                            }
                        }
                    }
                }
//                System.out.println(portNameList);
                for(String linkpoint:linkpointNameList){
                    if(!portNameList.contains(linkpoint)){
                        System.out.println("AADL模型中，组件" + componentName + "的端口" +linkpoint+"在其子组件中未包含" );
                    }

                }
            }
        }

        System.out.println("-----------------------------------");

        for (String componentId : componentSysmlList) {
            Map<String, Component> sub_1 = componentListSysml.get(componentId).getSubComponentList();
            String componentName = componentListSysml.get(componentId).getAttr("name");
//            System.out.println(componentName);

            List<Linkpoint> linkpointList = componentListSysml.get(componentId).getLinkpointList();
            List<String> linkpointNameList = new LinkedList<>();
            for (Linkpoint linkpoint : linkpointList) {
                linkpointNameList.add(linkpoint.getAttr("name"));
            }
            List<String> portNameList=new LinkedList<>();
            String taskName="";
            if (sub_1 != null) {
                List<String> subList = keySet(sub_1);
                for (String task : subList) {
                    taskName= sub_1.get(task).getAttr("Name");
                    List<Port> portList = sub_1.get(task).getPortList();

                    for (Port port : portList) {
                        portNameList.add(port.getAttr("name"));
                    }
                }
                for(String linkpoint:linkpointNameList){
                    if(!portNameList.contains(linkpoint)){
                        System.out.println("Sysml模型中，组件" + componentName + "的端口" +linkpoint+"在其子组件中未包含" );
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        excute();
    }
}
