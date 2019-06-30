package ruleEntity.base;

import static utils.XMLParseUtil.parseXML;

import java.util.LinkedHashMap;
import java.util.Map;

import entity.Channel;
import entity.Component;
import entity.Linkpoint;

public class LinkPointDirectionConsistency {

	public static void excute() {
		Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();
        
        parseXML("sysml0629.xml", componentListSysml, channelListSysml);
//      System.out.println("\naadl存储的结果为：");
//		for (String componentKey : componentListAadl.keySet()) {
//			System.out.println("\nComponent id : " + componentKey);
//			componentListAadl.get(componentKey).attrsToString();
//			for (String subComponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
//				System.out.println("\nsubComponent id : " + subComponentKey);
//				componentListAadl.get(componentKey).getSubComponentList().get(subComponentKey).attrsToString();
//			}
//		}
        
        System.out.println("sysml:");
        linkPointDirectionCheck(componentListSysml, channelListSysml);
	}

	
	private static void linkPointDirectionCheck(Map<String, Component> componentList,
			Map<String, Channel> channelList) {
		for (String channelKey : channelList.keySet()) {
        	Channel currentChannel = channelList.get(channelKey);
			String sourceId = currentChannel.getAttr("source"); 
			String destId = currentChannel.getAttr("dest");
			Linkpoint sourceLinkpoint = null, destLinkpoint = null;
			for (String componentKey : componentList.keySet()) {
				if (componentList.get(componentKey).getLinkpointList().size() > 0) {
					for (Linkpoint linkpoint : componentList.get(componentKey).getLinkpointList()) {
						if (linkpoint.getAttr("id").equals(sourceId)) {
							sourceLinkpoint = linkpoint;
						}
						if (linkpoint.getAttr("id").equals(destId)) {
							destLinkpoint = linkpoint;
						}
					}
				}
			}
			//System.out.println("source id is " + sourceId + " dest id is " + destId);
			if (destLinkpoint == null || sourceLinkpoint == null) continue;

			checkTargetProperty(sourceLinkpoint, destLinkpoint, currentChannel, "direction");
		}
	}
	
	private static void checkTargetProperty(Linkpoint sourceLinkpoint, 
									 Linkpoint destLinkpoint, 
									 Channel currentChannel,
									 String targetProperty) {
		
		if (sourceLinkpoint.getAttr(targetProperty) == null) {
			sourceLinkpoint.attrsToString();
			System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
					sourceLinkpoint.getAttr("id") + " " + 
					sourceLinkpoint.getAttr("name") + " does not have " + targetProperty + ".");
		} else if (destLinkpoint.getAttr(targetProperty) == null) {
			System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
					destLinkpoint.getAttr("id") + " " + 
					destLinkpoint.getAttr("name") + " does not have " + targetProperty + ".");
		} else {
			if (sourceLinkpoint.getAttr(targetProperty).contains("in") && 
					destLinkpoint.getAttr(targetProperty).contains("out")) {
				return;
			}
			if (sourceLinkpoint.getAttr(targetProperty).contains("out") &&
					destLinkpoint.getAttr(targetProperty).contains("in")) {
				return;
			}
			System.out.println("Channel " + currentChannel.getAttr("id") + 
					" : source linkpoint's " + targetProperty + " " + 
					sourceLinkpoint.getAttr(targetProperty) + 
					" is inconsistent with dest linkpoint's " + targetProperty + " " + 
					destLinkpoint.getAttr(targetProperty));
//			if (!sourceLinkpoint.getAttr(targetProperty).equals(destLinkpoint.getAttr(targetProperty))) {
//				System.out.println("Channel " + currentChannel.getAttr("id") + 
//						" : source linkpoint's " + targetProperty + " " + 
//						sourceLinkpoint.getAttr(targetProperty) + 
//						" is inconsistent with dest linkpoint's " + targetProperty + " " + 
//						destLinkpoint.getAttr(targetProperty));
//			}
		}
	}
	
	public static void main(String[] args) {
		excute();
	}
	
}
