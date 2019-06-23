package ruleEntity.safety;

import utils.XMLParseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RuleSafetyLevelCommunication {
    private String name = "ComponentSafetyLevel";
    private String type = "safety";

    public static void excute() {
        List<HashMap<String, String>> list = new ArrayList<>();
        //list = XMLParseUtil.parseXML("FlightSystem.xml");
        for (HashMap<String, String> hash : list) {
            String tmpClass = hash.get("class");
            if (tmpClass.equals("component") && ("design").equals(hash.get("stage")) && ("source").equals(hash.get("link"))) {
                String componentSource = hash.get("name");
                String safetyLevel = hash.get("safetyLevel");

                String communication = hash.get("communication");

                for (HashMap<String, String> hashNew : list) {

                    if (communication.equals(hashNew.get("communication")) && (hashNew.get("link").equals("dest"))) {
                        if (safetyLevel.compareTo(hashNew.get("safetyLevel"))<0) {
                            String componentDest=hashNew.get("name");
                            System.out.println("组件"+componentSource + "的安全级别低于"+"组件"+componentDest+"的安全级别，不能进行信息交互");
//                            System.out.print(hashNew);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        RuleSafetyLevelCommunication.excute();
    }
}
