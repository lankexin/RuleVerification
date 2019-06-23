package ruleEntity.safety;

import utils.XMLParseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RuleComponentSafetyLevel {
	private String name = "ComponentSafetyLevel";
	private String type = "safety";
	
    public static void excute() {
        List<HashMap<String,String>> list=new ArrayList<>();
        //list=XMLParseUtil.parseXML("FlightSystem.xml");
        for(HashMap<String,String> hash:list){
            String tmpClass=hash.get("class");
            if(tmpClass.equals("component")){
                String componentName=hash.get("name");
                String safetyLevel=hash.get("safetyLevel");
                String stage=hash.get("stage");

                hash.put("class","checkedComponent");

                for(HashMap<String,String> hashNew:list){

                   if(componentName.equals(hashNew.get("name"))&&(!hashNew.get("class").equals("checkedComponent"))) {
                        if(!safetyLevel.equals(hashNew.get("safetyLevel"))){
                            System.out.println("组件"+componentName+"需求和设计间的安全级别不一致");
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
}
