package ruleEntity.realTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.XMLParseUtil;

public class RuleMessagePeriod {
	
	private String name = "Component wcet and Message period";
	private String type = "realtime";
	
	public static void excute() {
        List<HashMap<String, String>> list = new ArrayList<>();
        //list = XMLParseUtil.parseXML("FlightSystem.xml");

        for (HashMap<String, String> hash : list) {
            String tmpClass = hash.get("class");
            if (tmpClass.equals("component") && hash.get("receivedMessage") != null) {
            	String componentName = hash.get("name");
            	String messageName = hash.get("receivedMessage");
            	int componentWcet = Integer.valueOf(hash.get("wcet"));

                String communication = hash.get("communication");

                for (HashMap<String, String> hashNew : list) {
                    if (hashNew.get("class").equals("message") && messageName.equals(hashNew.get("name"))) {
                        int messagePeriod = Integer.valueOf(hashNew.get("period"));
                        if (messagePeriod <= componentWcet) {
                            System.out.println("组件"+componentName + "的执行时间" + componentWcet + 
                            		"不小于"+messageName+"的周期" + messagePeriod + "，不存在不一致问题");
                        } else {
                        	System.out.println("组件"+componentName + "的执行时间" + componentWcet + 
                            		"小于"+messageName+"的周期" + messagePeriod + "，存在不一致问题");
                        }
                    }
                }
            }
        }
    }
	
	public String getName() {
		return name;
	}
	
}
