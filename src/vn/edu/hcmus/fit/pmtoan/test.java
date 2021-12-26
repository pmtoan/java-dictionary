//package vn.edu.hcmus.fit.pmtoan;
//
///**
// * vn.edu.hcmus.fit.pmtoan
// * Create by pmtoan
// * Date 12/24/2021 - 2:28 PM
// * Description: ...
// */
//
//import org.apache.commons.collections.MultiHashMap;
//import java.util.Set;
//import java.util.Map;
//import java.util.Iterator;
//import java.util.List;
//public class MultiMapExample {
//
//    public static void main(String[] args) {
//        MultiHashMap mp=new MultiHashMap();
//        mp.put("a", 10);
//        mp.put("a", 11);
//        mp.put("a", 12);
//        mp.put("b", 13);
//        mp.put("c", 14);
//        mp.put("e", 15);
//        List list = null;
//
//        Set set = mp.entrySet();
//        Iterator i = set.iterator();
//        while(i.hasNext()) {
//            Map.Entry me = (Map.Entry)i.next();
//            list=(List)mp.get(me.getKey());
//
//            for(int j=0;j<list.size();j++)
//            {
//                System.out.println(me.getKey()+": value :"+list.get(j));
//            }
//        }
//    }
//}