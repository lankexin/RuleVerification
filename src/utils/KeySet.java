package utils;

import java.util.*;

public class KeySet {
    public static <T> List<String> keySet(Map<String,T> map){
        List<String> list=new LinkedList<>();
        Set<String> set = map.keySet();
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            list.add(str);
        }
        return list;
    }
}
