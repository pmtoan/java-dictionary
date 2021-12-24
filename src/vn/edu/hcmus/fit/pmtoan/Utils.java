package vn.edu.hcmus.fit.pmtoan;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
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
            if(key.toLowerCase(Locale.ROOT).contains(pattern.toLowerCase(Locale.ROOT))){
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

    public static void saveEditFile(String pathFile, Map<String, String> dictionary){
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

    public static List<String> readHistoryFile(String pathFile){
        List<String> list = new ArrayList<>();
        try {
            File file = new File(pathFile);
            if(!file.isFile()){
                file.createNewFile();
            } else{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line = bufferedReader.readLine();
                if(line != null){
                    String[] split = line.split("`");

                    for(String key : split)
                    {
                        list.add(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveHistorySearch(String pathFile, String key, boolean append){
        try {
            File file = new File(pathFile);
            if (!file.isFile()) {
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(file, Charset.defaultCharset(), append));

            String data = key.equals("") ? key : (key + "`");

            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        
    }
}
