package vetityfy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static utils.PackageUtil.getClasssFromPackage;

public class RuleVerification {
    /**
     * 此处类型分为base，safety，realTime
     * @param type
     */
    public static void verify(String type)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Class item : getClasssFromPackage("ruleEntity."+type)) {
            Method method = item.getDeclaredMethod("excute");
            System.out.println(item);
            method.invoke(null);
        }
    }

    public static void main(String[] args)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        RuleVerification.verify("base");

    }
}
