package vn.edu.hcmus.fit.pmtoan;

import java.io.*;
import java.util.*;

/**
 * vn.edu.hcmus.fit.pmtoan
 * Create by pmtoan
 * Date 12/21/2021 - 1:07 PM
 * Description: ...
 */
public class Utils {
    public static Map<String, String> readOriginFile(String pathFile){
        Map<String, String> map = new HashMap<>();
        try {
            File file = new File(pathFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                String[] split = line.split("`");
                if(split.length != 2)
                    continue;
                map.put(split[0], split[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static void readEditFile(String pathFile, Map<String, String> dictionary){
        try {
            File file = new File(pathFile);
            if(!file.isFile()){
                file.createNewFile();
            } else{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                String line;

                while((line = bufferedReader.readLine()) != null)
                {
                    String[] split = line.split("`");
                    if(split.length != 2)
                        continue;
                    dictionary.put(split[0], split[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> searchBySlang(String pattern, HashSet<String> keySet){
        List<String> list = new ArrayList<>();

        for(String key : keySet){
            if(key.contains(pattern)){
                list.add(key);
            }
        }

        return list;
    }

    public static List<String> searchByDefinition(String pattern, Map<String, String> dictionary){
        List<String> list = new ArrayList<>();

        for(String key : dictionary.keySet()){
            if(dictionary.get(key).toLowerCase(Locale.ROOT).contains(pattern.toLowerCase(Locale.ROOT))){
                list.add(key);
            }
        }

        return list;
    }


    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        String st = s.nextLine();
        if(st.equals("ok")){
            long startTime = System.currentTimeMillis();
            Map<String, String> map = readOriginFile("s.txt");
            long middle = System.currentTimeMillis();

            HashSet<String> keySet = new HashSet<>(map.keySet());
            List<String> listResult = searchBySlang("mai", keySet);

            System.out.println("Read file: " + (middle - startTime));
            System.out.println("Search: " + (System.currentTimeMillis() - middle));
            System.out.println("Result size: " + listResult.size());
        }


    }
}
