//package dynamicSimulation;
//
//import entity.Component;
//import entity.Linkpoint;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DynamicSimulation {
//    public void findFault(){
////        Linkpoint linkpoint1=new Linkpoint("input1_1","message<5","to output1_1");
////        Linkpoint linkpoint2=new Linkpoint("output1_1","","to component2 input2_1 ");
////        Linkpoint linkpoint3=new Linkpoint("input1_2","message<5","to output1_2");
////        Linkpoint linkpoint4=new Linkpoint("output1_2","","to component3 input3_1");
////        Linkpoint linkpoint5=new Linkpoint("input2_1","message<5","to output2_1");
////        Linkpoint linkpoint6=new Linkpoint("output2_1","","to end");
////
////        Linkpoint linkpoint7=new Linkpoint("input3_1","message<5","to output3_1");
////        Linkpoint linkpoint8=new Linkpoint("output3_1","","to end");
//
//        Map<String,Linkpoint> linkpointMap=new HashMap<>();
//
//        linkpointMap.put(linkpoint1.getName(),linkpoint1);
//        linkpointMap.put(linkpoint2.getName(),linkpoint2);
//        linkpointMap.put(linkpoint3.getName(),linkpoint3);
//        linkpointMap.put(linkpoint4.getName(),linkpoint4);
//        linkpointMap.put(linkpoint5.getName(),linkpoint5);
//        linkpointMap.put(linkpoint6.getName(),linkpoint6);
//        linkpointMap.put(linkpoint7.getName(),linkpoint7);
//        linkpointMap.put(linkpoint8.getName(),linkpoint8);
//
//        List<Linkpoint> list1=new ArrayList<>();
//        list1.add(linkpoint1);
//        list1.add(linkpoint3);
//
//        List<Linkpoint> list2=new ArrayList<>();
//        list2.add(linkpoint2);
//        list2.add(linkpoint4);
//
//        List<Linkpoint> list3=new ArrayList<>();
//        list3.add(linkpoint5);
//
//        List<Linkpoint> list4=new ArrayList<>();
//        list4.add(linkpoint6);
//
//        List<Linkpoint> list5=new ArrayList<>();
//        list3.add(linkpoint7);
//
//        List<Linkpoint> list6=new ArrayList<>();
//        list4.add(linkpoint8);
//
//        Component component_1=new Component("component1",list1,list2);
//        Component component_2=new Component("component2",list3,list4);
//        Component component_3=new Component("component3",list5,list6);
//
//
//        Map<String,Component> componentMap=new HashMap<>();
//        componentMap.put(component_1.getName(),component_1);
//        componentMap.put(component_2.getName(),component_2);
//        componentMap.put(component_3.getName(),component_3);
//
//        dynamic(componentMap,"component1",linkpointMap);
//
//        }
//
//
//    public void dynamic(Map<String,Component> componentMap,String componentname,Map<String,Linkpoint> linkpointMap){
//        Component component=componentMap.get(componentname);
//        String flag="";
//        List<Linkpoint> list=component.getInputLink();
//        for (Linkpoint link : list) {
//            Component currentcomponent=component;
//            Linkpoint currentLink=link;
//            while (true) {
//                String inpost=currentLink.getPostcondition();
////                System.out.println(inpost);
//                String[] toOut=inpost.split(" ");
//                String out=toOut[1];
////                System.out.println(out);
////                if(out.equals("end"))
////                    break;
////                if(!component.getOutputLink().contains(out))
////                    System.out.println("当前出口不是本组件的出口");
////                System.out.println(currentcomponent);
////                System.out.println(currentLink);
//                System.out.println("组件"+currentcomponent.getName()+"端口"+currentLink.getName());
//                Linkpoint outLink=linkpointMap.get(out);
//                System.out.println("组件"+currentcomponent.getName()+"端口"+outLink.getName());
//                String outpost=outLink.getPostcondition();
////                System.out.println(outpost);
//                String[] toComponent=outpost.split(" ");
//
//                String nextComponent=toComponent[1];
////                System.out.println(nextComponent);
//                if(nextComponent.equals("end"))
//                    break;
//                else {
//                    String nextLink=toComponent[2];
////                    System.out.println(toComponent[2]);
//                    currentcomponent=componentMap.get(nextComponent);
////                    System.out.println(nextLink);
////                    System.out.println(currentcomponent);
//                    currentLink=linkpointMap.get(nextLink);
//                }
//
//            }
//        }
//    }
//    public static void main(String[] args){
//        DynamicSimulation a=new DynamicSimulation();
//        a.findFault();
//    }
//}
