package vn.edu.hcmus.fit.pmtoan;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * vn.edu.hcmus.fit.pmtoan
 * Create by pmtoan
 * Date 12/21/2021 - 1:07 PM
 * Description: ...
 */
public class Utils {
    public static Map<String, List<String>> readOriginFile(String pathFile){
        Map<String, List<String>> dictionary = new HashMap<>();
        try {
            File file = new File(pathFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null)
            {
                String[] split = line.split("`");
                if(split.length != 2)
                    continue;

                List<String> values = new ArrayList<>();
                for(String value : split[1].split("\\|")){
                    values.add(value.trim());
                }
                System.out.println(split[0]);
                System.out.println(values);

                dictionary.put(split[0], values);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionary;
    }

    public static void cloneOriginFile(String pathOrigin, String pathClone){
        try {
            File cloneFile = new File(pathClone);
            if(!cloneFile.isFile()){
                cloneFile.createNewFile();
            }

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(pathOrigin));
            byte[] bytes_array = bufferedInputStream.readAllBytes();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathClone));
            bufferedOutputStream.write(bytes_array);
            bufferedOutputStream.flush();

            bufferedInputStream.close();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> readCloneFile(String pathOrigin, String pathClone){
        Map<String, List<String>> dictionary = new HashMap<>();
        try {
            File cloneFile = new File(pathClone);
            if(!cloneFile.isFile()){
                cloneOriginFile(pathOrigin, pathClone);
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathClone));

            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                String[] split = line.split("`");
                if(split.length != 2)
                    continue;

                List<String> values = new ArrayList<>();

                if(dictionary.containsKey(split[0])){
                    values = dictionary.get(split[0]);
                }

                for(String value : split[1].split("\\|")){
                    values.add(value.trim());
                }

                dictionary.put(split[0], values);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionary;
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

    public static List<String> searchByDefinition(String pattern, Map<String, List<String>> dictionary){
        List<String> list = new ArrayList<>();

        for(String key : dictionary.keySet()){
            for(String value : dictionary.get(key)){
                if(value.toLowerCase().contains(pattern.toLowerCase())){
                    list.add(key);
                }
            }
        }

        return list;
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
                bufferedReader.close();
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
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(pathFile, append));

            String data = key.equals("") ? key : (key + "`");

            bufferedOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addNewSlang(String pathFile,String[] data){
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(pathFile, true));

            bufferedOutputStream.write((data[0] + "`" + data[1] + "\n").getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overwriteAllSlang(String pathFile, String[] oldSlang, String[] newSlang){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile));

            String line;
            String data_string = "";
            boolean found = false;

            while((line = bufferedReader.readLine()) != null)
            {
                if(line.contains(oldSlang[0])){
                    if(!found){
                        line = newSlang[0] + "`" + newSlang[1];
                        found = true;
                    } else{
                        continue;
                    }

                }
                data_string += line + "\n";
            }

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathFile));
            bufferedOutputStream.write(data_string.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();

            bufferedReader.close();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateSlang(String pathFile, String[] oldSlang, String[] newSlang){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile));

            String line;
            String data_string = "";

            while((line = bufferedReader.readLine()) != null)
            {
                if(line.contains(oldSlang[0]) && line.contains(oldSlang[1])){
                    line = newSlang[0] + "`" + newSlang[1];
                }
                data_string += line + "\n";
            }

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathFile));
            bufferedOutputStream.write(data_string.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();

            bufferedReader.close();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        Map<String, List<String>>  dictionary = readCloneFile("slang.txt", "slang_clone.txt");

        String[] data = {"AAB","Average At Best"};
        String[] new_data = {"AAB","Number one duplicate x999"};

        //cloneOriginFile("slang.txt", "slang_clone.txt");
        addNewSlang("slang_clone.txt", data);
        //overwriteAllSlang("slang_clone.txt", data, new_data);

        System.out.println("Duration: " + (System.currentTimeMillis() - start));
    }
}
