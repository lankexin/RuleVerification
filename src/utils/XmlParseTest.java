package utils;

import java.util.HashMap;
import java.util.Map;

import entity.Channel;
import entity.Component;

public class XmlParseTest {
	
	public static void main(String[] args) {
		Map<String, Component> componentListSimulink = new HashMap<>();
		Map<String, Channel> channelListSimulink = new HashMap<>();
		
		Map<String, Component> componentListAadl = new HashMap<>();
		Map<String, Channel> channelListAadl = new HashMap<>();
		
		Map<String, Component> componentListSysml = new HashMap<>();
		Map<String, Channel> channelListSysml = new HashMap<>();
		
		XMLParseUtil.parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);
		XMLParseUtil.parseXML("aadl(1).xml", componentListAadl, channelListAadl);
		XMLParseUtil.parseXML("sysml(1).xml", componentListSysml, channelListSysml);
		
		System.out.println("\ntest simulink存储的结果为：");
		for (String componentKey : componentListSimulink.keySet()) {
			System.out.println("\nComponent id : " + componentKey);
			componentListSimulink.get(componentKey).attrsToString();
			for (String stateKey : componentListSimulink.get(componentKey).getStateList().keySet()) {
				System.out.println("State id : " + stateKey);
				componentListSimulink.get(componentKey).getStateList().get(stateKey).attrsToString();
				System.out.println(componentListSimulink.get(componentKey).getStateList().get(stateKey)
						.getAttr("faultType") + "\n");
			}
		}
		
		System.out.println("\ntest sysml存储的结果为：");
		for (String componentKey : componentListSysml.keySet()) {
			System.out.println("\nComponent id : " + componentKey);
			componentListSysml.get(componentKey).attrsToString();
			for (String exceptionKey : componentListSysml.get(componentKey).getExceptionList().keySet()) {
				System.out.println("Exception id : " + exceptionKey);
				componentListSysml.get(componentKey).getExceptionList().get(exceptionKey).attrsToString();
//				System.out.println(componentListSimulink.get(componentKey).getStateList().get(exceptionKey)
//						.getAttr("faultType") + "\n");
			}
		}
		
		System.out.println("\ntest aadl存储的结果为：");
		for (String componentKey : componentListAadl.keySet()) {
			System.out.println("\nComponent id : " + componentKey);
			componentListAadl.get(componentKey).attrsToString();
			for (String propagationKey : componentListAadl.get(componentKey).getPropagationList().keySet()) {
				System.out.println("Propagation id : " + propagationKey);
				componentListAadl.get(componentKey).getPropagationList().get(propagationKey).attrsToString();
//				System.out.println(componentListSimulink.get(componentKey).getStateList().get(propagationKey)
//						.getAttr("faultType") + "\n");
			}
		}
	}

}
