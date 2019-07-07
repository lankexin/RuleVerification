package ruleEntity.base;

import entity.Channel;
import entity.Component;
import entity.Linkpoint;

import java.util.LinkedHashMap;
import java.util.Map;

import static utils.XMLParseUtil.parseXML;

/*
* Author：lankx
* 手动验证文档，一致性规则，第4条
* */

public class MessageNameConsistency {
	
	public static void excute() {
		Map<String, Component> componentListSysml = new LinkedHashMap<>();
        Map<String, Channel> channelListSysml = new LinkedHashMap<>();
        
        Map<String, Component> componentListAadl = new LinkedHashMap<>();
        Map<String, Channel> channelListAadl = new LinkedHashMap<>();
        
        Map<String, Component> componentListSimulink = new LinkedHashMap<>();
        Map<String, Channel> channelListSimulink = new LinkedHashMap<>();
        
        parseXML("sysml0629.xml", componentListSysml, channelListSysml);
        parseXML("simulink0703.xml", componentListSimulink, channelListSimulink);
        parseXML("aadl(9).xml", componentListAadl, channelListAadl);
        
        System.out.println("sysml:");
        messageNameCheck(componentListSysml, channelListSysml);
        System.out.println("aadl:");
        messageNameCheck(componentListAadl, channelListAadl);
        System.out.println("simulink:");
        messageNameCheck(componentListSimulink, channelListSimulink);
	}

	
	private static void messageNameCheck(Map<String, Component> componentList,
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

			checkTargetProperty(sourceLinkpoint, destLinkpoint, currentChannel, "name");
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
					sourceLinkpoint.getAttr("name") + " does not have " + 
					targetProperty + ".");
		} else if (destLinkpoint.getAttr(targetProperty) == null) {
			System.out.println("Channel " + currentChannel.getAttr("id") + " : " + 
					destLinkpoint.getAttr("id") + " " + 
					destLinkpoint.getAttr("name") + " does not have " + 
					targetProperty + ".");
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
