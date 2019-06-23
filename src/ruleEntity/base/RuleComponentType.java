package ruleEntity.base;

import ruleEntity.safety.RuleComponentSafetyLevel;
import utils.XMLParseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RuleComponentType {
	private String name = "ComponentType";
	private String type = "base";

    public static void excute() {
        List<HashMap<String, String>> list = new ArrayList<>();
        //list = XMLParseUtil.parseXML("FlightSystem.xml");
        for (HashMap<String, String> hash : list) {
            String tmpClass = hash.get("class");
            if (tmpClass.equals("component")) {
                String componentName = hash.get("name");
                String safetyLevel = hash.get("type");
                String stage = hash.get("stage");

                hash.put("class", "checkedComponent");

                for (HashMap<String, String> hashNew : list) {

                    if (componentName.equals(hashNew.get("name")) && (!hashNew.get("class").equals("checkedComponent"))) {
                        if (!safetyLevel.equals(hashNew.get("type"))) {
                            System.out.println("组件" + componentName + "需求和设计间的组件类型不一致");
//                            System.out.print(hashNew);
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args){
        RuleComponentSafetyLevel.excute();
    }
    
    public String getName() {
    	return name;
    }
}
