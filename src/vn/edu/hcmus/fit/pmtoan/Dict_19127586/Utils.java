package vn.edu.hcmus.fit.pmtoan.Dict_19127586;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * vn.edu.hcmus.fit.pmtoan
 * Create by pmtoan
 * Date 12/21/2021 - 1:07 PM
 * Description: ...
 */
public class Utils {
    public static Map<String, List<String>> readFileToDictionary(String pathFile){
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

    public static void deleteCloneFile(String pathClone) {
        File cloneFile = new File(pathClone);
        if (cloneFile.isFile()) {
            cloneFile.delete();
        }
    }

    public static Map<String, List<String>> readCloneFile(String pathOrigin, String pathClone){
        File cloneFile = new File(pathClone);
        if(!cloneFile.isFile()){
            cloneOriginFile(pathOrigin, pathClone);
        }

        Map<String, List<String>> dictionary = readFileToDictionary(pathClone);

        return dictionary;
    }

    public static List<Dictionary> readCloneFileToTable(String pathOrigin, String pathClone){
        List<Dictionary> dictionary = new ArrayList<>();
        try {
            File cloneFile = new File(pathClone);
            if(!cloneFile.isFile()){
                cloneOriginFile(pathOrigin, pathClone);
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathClone, StandardCharsets.UTF_8));
            bufferedReader.readLine();
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                String[] split = line.split("`");
                if(split.length != 2)
                    continue;

                dictionary.add(new Dictionary(split[0], split[1]));
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
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
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

    public static void addNewSlang(String pathFile, String slang, String definition){
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(pathFile, true));

            bufferedOutputStream.write((slang + "`" + definition + "\n").getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overwriteAllSlang(String pathFile, String oldSlang, String newDefinition){
        try {
            File tempFile = File.createTempFile("tmp", "");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile, StandardCharsets.UTF_8));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

            String line;
            boolean found = false;

            while((line = bufferedReader.readLine()) != null)
            {
                if(line.startsWith(oldSlang)) {
                    String[] split = line.split("`");
                    if(split[0].equals(oldSlang)) {
                        if (!found) {
                            line = oldSlang + "`" + newDefinition;
                            found = true;
                        } else {
                            continue;
                        }
                    }
                }
                line += "\n";
                bufferedOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
                bufferedOutputStream.flush();
            }

            bufferedReader.close();
            bufferedOutputStream.close();

            File myFile = new File(pathFile);
            myFile.delete();
            tempFile.renameTo(new File(pathFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateSlang(String pathFile,  String oldSlang, String oldDefinition, String newDefinition){
        try {
            File tempFile = File.createTempFile("tmp", "");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile, StandardCharsets.UTF_8));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                if(line.startsWith(oldSlang)) {
                    String[] split = line.split("`");
                    if (split[0].equals(oldSlang) && split[1].equals(oldDefinition)) {
                        line = oldSlang + "`" + newDefinition;
                    }
                }
                line += "\n";
                bufferedOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
                bufferedOutputStream.flush();
            }

            bufferedReader.close();
            bufferedOutputStream.close();

            File myFile = new File(pathFile);
            myFile.delete();
            tempFile.renameTo(new File(pathFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSlang(String pathFile, String slang, String definition){
        try {
            File tempFile = File.createTempFile("tmp", "");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile, StandardCharsets.UTF_8));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                if(line.startsWith(slang)) {
                    String[] split = line.split("`");
                    if(split[0].equals(slang) && split[1].equals(definition)){
                        continue;
                    }
                }

                line += "\n";
                bufferedOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
                bufferedOutputStream.flush();
            }

            bufferedReader.close();
            bufferedOutputStream.close();

            File myFile = new File(pathFile);
            myFile.delete();
            tempFile.renameTo(new File(pathFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
