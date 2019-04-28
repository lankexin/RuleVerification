package vetityfy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static utils.PackageUtil.getClasssFromPackage;

public class RuleVerification {
    /**
     * 此处类型分为base，safety，realTime,all
     * @param type
     */
    public static void verify(String type)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(type.equals("all")) {
            for (Class item : getClasssFromPackage("ruleEntity")) {
                Method method = item.getDeclaredMethod("excute");
                System.out.println(item);
                method.invoke(null);
            }
        }
        else{
            for (Class item : getClasssFromPackage("ruleEntity"+type)) {
                Method method = item.getDeclaredMethod("excute");
                System.out.println(item);
                method.invoke(null);
            }
        }
    }

    public static void main(String[] args)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        RuleVerification.verify("all");
    }
}
