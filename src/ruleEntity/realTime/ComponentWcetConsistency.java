package ruleEntity.realTime;

import static utils.XMLParseUtil.parseXML;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entity.Channel;
import entity.Component;

public class ComponentWcetConsistency {

	
	public static void excute() {
		Map<String, Component> componentListAadl = new LinkedHashMap<>();
        Map<String, Channel> channelListAadl = new LinkedHashMap<>();
        
        parseXML("aadl(3).xml", componentListAadl, channelListAadl);
        
        for (String componentKey : componentListAadl.keySet()) {
        	if (componentListAadl.get(componentKey).getSubComponentList().size() > 0) {
        		
        	}
        }
	}
	
	private static void getFlow() {
		
	}
}
