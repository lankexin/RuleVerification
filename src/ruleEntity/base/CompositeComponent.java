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

        XMLParseUtil.parseXML("aadl(1).xml", componentListAadl, channelListAadl);
        XMLParseUtil.parseXML("sysml(4).xml", componentListSysml, channelListSysml);

        List<String> componentAADLList = new ArrayList<>();
        componentAADLList = keySet(componentListAadl);

        List<String> componentSysmlList = new ArrayList<>();
        componentSysmlList = keySet(componentListSysml);
//        System.out.println(componentList);

        for (String componentId : componentAADLList) {
            Map<String, Component> sub_1 = componentListAadl.get(componentId).getSubComponentList();
            String componentName = componentListAadl.get(componentId).getAttr("name");
            System.out.println(componentName);
            List<Linkpoint> linkpointList = componentListAadl.get(componentId).getLinkpointList();
            List<String> linkpointNameList = new LinkedList<>();
            for (Linkpoint linkpoint : linkpointList) {
                linkpointNameList.add(linkpoint.getAttr("name"));
            }
            if (sub_1 != null) {
                List<String> subList = keySet(sub_1);
                for (String sub : subList) {
                    Map<String, Component> sub_2 = sub_1.get(sub).getSubComponentList();
                    if (sub_2 != null) {
                        List<String> subtaskList = keySet(sub_2);
                        for (String task : subtaskList) {
//                            System.out.println(task);

                            String taskName = sub_2.get(task).getAttr("Name");
                            List<Port> portList = sub_2.get(task).getPortList();
                            for (Port port : portList) {
                                String portName = port.getAttr("name");
//                                System.out.println("port" + portName);
                                if (!linkpointNameList.contains(portName))
                                    System.out.println("AADL模型中，组件" + componentName + "未包含其子组件"
                                            + taskName + "的端口" + portName);
                            }

                        }
                    }
                }
            }
        }


        for (String componentId : componentSysmlList) {
            Map<String, Component> sub_1 = componentListSysml.get(componentId).getSubComponentList();
            String componentName = componentListSysml.get(componentId).getAttr("name");
            System.out.println(componentName);

            List<Linkpoint> linkpointList = componentListSysml.get(componentId).getLinkpointList();
            List<String> linkpointNameList = new LinkedList<>();
            for (Linkpoint linkpoint : linkpointList) {
                linkpointNameList.add(linkpoint.getAttr("name"));
            }
            if (sub_1 != null) {
                List<String> subList = keySet(sub_1);
                for (String task : subList) {
                    String taskName = sub_1.get(task).getAttr("Name");
                    List<Port> portList = sub_1.get(task).getPortList();
                    for (Port port : portList) {
                        String portName = port.getAttr("name");
//                                System.out.println("port" + portName);
                        if (!linkpointNameList.contains(portName))
                            System.out.println("Sysml模型中，组件" + componentName + "未包含其子组件" + taskName + "的端口" +
                                    portName);
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        excute();
    }
}
