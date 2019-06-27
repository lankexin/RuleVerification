import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import verityfy.RuleVerification;


public class Script {
    public static void Script() throws NoSuchMethodException, IllegalAccessException,InvocationTargetException{
        List<String> typeList = new ArrayList<>();
        typeList.add("all");
        typeList.add("base");
        typeList.add("safety");
        typeList.add("realTime");


        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Please Enter command:");
            String command = sc.nextLine();
            if (command.equals("exit")) {
                break;
            }
            String[] verifyType = command.split(" ");
            String[] rule = verifyType[1].split("-");
            /**
             * verify type-safety 分类实现验证
             */

            if (rule[0].equals("type")) {
                if (!typeList.contains(rule[1]))
                    System.out.println("不支持当前类型验证");
                else
                    RuleVerification.verifyType(rule[1]);
            }
            /**
             * verify rule-safety.RuleComponentSafetyLevel  单条规则验证
             */
            if (rule[0].equals("rule")) {
                RuleVerification.verifyRule(rule[1]);
            }
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,InvocationTargetException{
        Script();
    }
}
