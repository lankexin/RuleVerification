package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import com.sun.xml.internal.bind.v2.TODO;

import entity.Channel;
import entity.Component;
import entity.ErrorPropagations;
import entity.ExceptionXML;
import entity.Linkpoint;
import entity.Propagation;
import entity.State;
import entity.Transition;

public class XMLParseUtil {
	
	public XMLParseUtil() {
		
	}
	
	public static void main(String[] args) {
		Map<String, Component> componentListSimulink = new HashMap<>();
		Map<String, Channel> channelListSimulink = new HashMap<>();
		
		Map<String, Component> componentListAadl = new HashMap<>();
		Map<String, Channel> channelListAadl = new HashMap<>();
		
		Map<String, Component> componentListSysml = new HashMap<>();
		Map<String, Channel> channelListSysml = new HashMap<>();
		
		parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);
		parseXML("aadl(1).xml", componentListAadl, channelListAadl);
		parseXML("sysml(1).xml", componentListSysml, channelListSysml);
		
		System.out.println("\nsimulink存储的结果为：");
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
		
		System.out.println("\nsysml存储的结果为：");
		for (String componentKey : componentListSysml.keySet()) {
			System.out.println("\nComponent id : " + componentKey);
			componentListSysml.get(componentKey).attrsToString();

			for (String exceptionKey : componentListSysml.get(componentKey).getExceptionList().keySet()) {
				System.out.println("Exception id : " + exceptionKey);
				componentListSysml.get(componentKey).getExceptionList().get(exceptionKey).attrsToString();
			}
		}
		
		System.out.println("\naadl存储的结果为：");
		for (String componentKey : componentListAadl.keySet()) {
			System.out.println("\nComponent id : " + componentKey);
			componentListAadl.get(componentKey).attrsToString();
			for (String propagationKey : componentListAadl.get(componentKey).getPropagationList().keySet()) {
				System.out.println("Propagation id : " + propagationKey);
				componentListAadl.get(componentKey).getPropagationList().get(propagationKey).attrsToString();
			}
		}
	}

	public static void parseXML(String inputPath,
			Map<String, Component> componentList,
			Map<String, Channel> channelList) {
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(inputPath);
			Element root = document.getRootElement();
			getXML(root, componentList, channelList);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private static void getXML(Element root,
			Map<String, Component> componentList,
			Map<String, Channel> channelList) {
		Iterator it = root.elementIterator();
		Element component = root;
		List<Attribute> componentAttrs = component.attributes();

		if (component.getName().equals("component")) {
			Component newComponent = new Component();
			for (Attribute attr : componentAttrs) {
//				System.out.print("属性名: " + attr.getName() + "   属性值: "
//						+ attr.getValue() + "\n");
				newComponent.setAttr(attr.getName(), attr.getValue());
			}
			componentList.put(newComponent.getAttr("id"), newComponent);
		} else if (component.getName().equals("system")) {
			Component newSystem = new Component();
			newSystem.setAttr("type", "system");
			for (Attribute attr : componentAttrs) {
				newSystem.setAttr(attr.getName(), attr.getValue());
			}
			componentList.put(newSystem.getAttr("type"), newSystem);
		} else if (component.getName().equals("communicationchannel")) {
			Channel newChannel = new Channel();
			for (Attribute attr : componentAttrs) {
				newChannel.setAttr(attr.getName(), attr.getValue());
			}
			channelList.put(newChannel.getAttr("id"), newChannel);
		} else if (component.getName().equals("linkpoint")) {
			String componentId = root.getParent().attribute("id").getValue();
			Linkpoint newLinkpoint = new Linkpoint();
			for (Attribute attr : componentAttrs) {
				newLinkpoint.setAttr(attr.getName(), attr.getValue());
			}
			componentList.get(componentId).getLinkpointList().add(newLinkpoint);
		} else if (component.getName().equals("transition")) {
			String componentId = root.getParent().attribute("id").getValue();
			Transition newTransition = new Transition();
			for (Attribute attr : componentAttrs) {
				newTransition.setAttr(attr.getName(), attr.getValue());
			}
			componentList.get(componentId).getTransitionList().add(newTransition);
		} else if (component.getName().equals("state")) {
			//System.out.println(component.attribute("id"));
			String componentId;
			State newState = new State();
			for (Attribute attr : componentAttrs) {
				newState.setAttr(attr.getName(), attr.getValue());
//				System.out.println(attr.getName() + " " + attr.getValue());
//				System.out.println("-------"+newState.getAttr("faultType"));
			}
			if (root.getParent().getName().equals("component"))  {
				componentId = root.getParent().attribute("id").getValue();
			}
			else {
				componentId = root.getParent().getParent().attribute("id").getValue();
				String stateId = root.getParent().attribute("id").getValue();
				State parentState = componentList.get(componentId).getStateList().get(stateId);
				parentState.getSubStateList().add(newState);
			}
			componentList.get(componentId).getStateList().put(newState.getAttr("id"), newState);
		} /*else if (component.getName().equals("error_propagations")) {
			String componentId = root.getParent().attribute("id").getValue();
			ErrorPropagations newErrorPropagations = new ErrorPropagations();
			for (Attribute attr : componentAttrs) {
				newErrorPropagations.setAttr(attr.getName(), attr.getValue());
			}
			componentList.get(componentId).getErrorPropagationList()
						.put(newErrorPropagations.getAttr("id"), newErrorPropagations);
		}*/ else if (component.getName().equals("propagation")) {
			String componentId;
			Propagation newPropagation = new Propagation();
			for (Attribute attr : componentAttrs) {
//				System.out.print("属性名: " + attr.getName() + "   属性值: "
//						+ attr.getValue() + "\n");
				newPropagation.setAttr(attr.getName(), attr.getValue());
				//System.out.println(attr.getName() + " " + attr.getValue());
			}
			componentId = root.getParent().getParent().attribute("id").getValue();
			componentList.get(componentId).getPropagationList()
						.put(newPropagation.getAttr("id"), newPropagation);
		} else if (component.getName().equals("exception")) {
			String componentId = root.getParent().attribute("id").getValue();
			ExceptionXML newException = new ExceptionXML();
			for (Attribute attr : componentAttrs) {
//				System.out.print("属性名: " + attr.getName() + "   属性值: "
//						+ attr.getValue() + "\n");
				(newException).setAttr(attr.getName(), attr.getValue());
			}
			componentList.get(componentId).getExceptionList()
					.put(newException.getAttr("name"), newException);
		}
		
		Iterator itt = component.elementIterator();
		
		while (itt.hasNext()) {
			Element componentChild = (Element) itt.next();
			getXML(componentChild, componentList, channelList);
		}
	}
	
}
