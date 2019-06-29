package ruleEntity.base;

import static utils.XMLParseUtil.parseXML;

import java.util.LinkedHashMap;
import java.util.Map;

import entity.Channel;
import entity.Component;
import entity.Linkpoint;

public class LinkPointTypeAndPeriod {

	public static void excute() {
		Map<String, Component> componentListAadl = new LinkedHashMap<>();
        Map<String, Channel> channelListAadl = new LinkedHashMap<>();
        
		Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();
        
		Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();
        
        parseXML("aadl(4).xml", componentListAadl, channelListAadl);
        parseXML("sysml(4).xml", componentListSysml, channelListSysml);
        parseXML("simulink(2).xml", componentListSimulink, channelListSimulink);
//      System.out.println("\naadl存储的结果为：");
//		for (String componentKey : componentListAadl.keySet()) {
//			System.out.println("\nComponent id : " + componentKey);
//			componentListAadl.get(componentKey).attrsToString();
//			for (String subComponentKey : componentListAadl.get(componentKey).getSubComponentList().keySet()) {
//				System.out.println("\nsubComponent id : " + subComponentKey);
//				componentListAadl.get(componentKey).getSubComponentList().get(subComponentKey).attrsToString();
//			}
//		}
        
        System.out.println("aadl:");
        linkPointTypeAndPeriodCheck(componentListAadl, channelListAadl);
        System.out.println("sysml:");
        linkPointTypeAndPeriodCheck(componentListSysml, channelListSysml);
        System.out.println("simulink:");
        linkPointTypeAndPeriodCheck(componentListSimulink, channelListSimulink);
	}

	
	private static void linkPointTypeAndPeriodCheck(Map<String, Component> componentList,
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
			
//			if (sourceLinkpoint.getAttr("period") == null) {
//				System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
//						sourceLinkpoint.getAttr("name") + " does not have period.");
//			} else if (destLinkpoint.getAttr("period") == null) {
//				System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
//						destLinkpoint.getAttr("name") + " does not have period.");
//			} else {
//				if (!sourceLinkpoint.getAttr("period").equals(destLinkpoint.getAttr("period"))) {
//					System.out.println("Channel " + currentChannel.getAttr("id") + 
//							" : source linkpoint's period " + sourceLinkpoint.getAttr("period") + 
//							" is inconsistent with dest linkpoint's period " + destLinkpoint.getAttr("period"));
//				}
//			}
			checkTargetProperty(sourceLinkpoint, destLinkpoint, currentChannel, "period");
			checkTargetProperty(sourceLinkpoint, destLinkpoint, currentChannel, "datatype");
		}
	}
	
	private static void checkTargetProperty(Linkpoint sourceLinkpoint, 
									 Linkpoint destLinkpoint, 
									 Channel currentChannel,
									 String targetProperty) {
		if (sourceLinkpoint.getAttr(targetProperty) == null) {
			System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
					sourceLinkpoint.getAttr("name") + " does not have " + targetProperty + ".");
		} else if (destLinkpoint.getAttr(targetProperty) == null) {
			System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
					destLinkpoint.getAttr("name") + " does not have " + targetProperty + ".");
		} else {
			if (!sourceLinkpoint.getAttr(targetProperty).equals(destLinkpoint.getAttr(targetProperty))) {
				System.out.println("Channel " + currentChannel.getAttr("id") + 
						" : source linkpoint's " + targetProperty + " " + 
						sourceLinkpoint.getAttr(targetProperty) + 
						" is inconsistent with dest linkpoint's " + targetProperty + " " + 
						destLinkpoint.getAttr(targetProperty));
			}
		}
	}
	
	public static void main(String[] args) {
		excute();
	}
	
}
