package verityfy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static utils.PackageUtil.getClasssFromPackage;

public class RuleVerification {
    /**
     * 此处类型分为base，safety，realTime, all
     * @param type
     */
    public static void verifyType(String type)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(type.equals("all")) {
            for (Class item : getClasssFromPackage("ruleEntity")) {
                Method method = item.getDeclaredMethod("excute");
                System.out.println(item);
                method.invoke(null);
            }
        }
        else{
            for (Class item : getClasssFromPackage("ruleEntity."+type)) {
                Method method = item.getDeclaredMethod("excute");
                System.out.println(item);
                method.invoke(null);
            }
        }
    }

    public static void verifyRule(String rule) throws IllegalAccessException, InvocationTargetException,
                                                        NoSuchMethodException {
        try {
            Class item = Class.forName("ruleEntity." + rule);
            System.out.println(item);
            Method method = item.getDeclaredMethod("excute");
            method.invoke(null);
        }catch(ClassNotFoundException e){
            System.out.println("不存在当前规则");
        }


    }

    public static void main(String[] args)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        RuleVerification.verifyType("safety");
//        String inputPath = "FlightSystem.xml";
//        XMLParseUtil mXMLParseUtil = new XMLParseUtil();
//        mXMLParseUtil.parseXML(inputPath);
        //System.out.print(mXMLParseUtil.parseXML(inputPath));
    }
}
