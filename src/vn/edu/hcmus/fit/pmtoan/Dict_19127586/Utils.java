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
    //-------------------------------------- Initial Data ---------------------------------------------\\
    public static void readFileToMap(String pathFile,
               Map<String, List<String>> slangMap, Map<String, List<String>> definitionMap){
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

                List<String> defList = new ArrayList<>();

                if(slangMap.containsKey(split[0])){
                    defList = slangMap.get(split[0]);
                }
                // this is def 1 | this is def 2 -> split[1]
                for(String value : split[1].split("\\|")){
                    defList.add(value.trim());
                }
                slangMap.put(split[0], defList);

                for(String value : split[1].split("\\|")){
                    for(String def : value.trim().split(" ")) {
                        List<String> slangList = new ArrayList<>();
                        String definition = def.trim().toLowerCase();

                        if(definitionMap.containsKey(definition)){
                            if(!definitionMap.get(definition).contains(split[0])){
                                definitionMap.get(definition).add(split[0]);
                            }
                        } else{
                            slangList.add(split[0]);
                            definitionMap.put(definition, slangList);
                        }
                    }
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cloneOriginFile(String pathOrigin, String pathClone){
        try {
            File cloneFile = new File(pathClone);
            if(!cloneFile.isFile()){
                cloneFile.createNewFile();
            }

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(pathOrigin));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathClone));

            byte[] buffer = new byte[1024*1024];
            int length;
            while ((length = bufferedInputStream.read(buffer)) > 0) {
                bufferedOutputStream.write(buffer, 0, length);
            }

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

    public static void readCloneFile(String pathOrigin, String pathClone,
                                   Map<String, List<String>> slangMap, Map<String, List<String>> definitionMap){
        File cloneFile = new File(pathClone);
        if(!cloneFile.isFile()){
            cloneOriginFile(pathOrigin, pathClone);
        }

        readFileToMap(pathClone, slangMap, definitionMap);
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

    //-------------------------------------- SEARCH ---------------------------------------------\\

    public static List<String> searchBySlang(String pattern, Trie keyStore){
        return keyStore.findSimilarWords(pattern);
    }

    public static List<String> searchByDefinition(String pattern, Map<String, List<String>> defMap){
        List<String> list = new ArrayList<>();

        String[] parse = pattern.trim().split(" ", 2);
        String firstElement = parse[0].toLowerCase();

        if(defMap.containsKey(firstElement)){
            list.addAll(defMap.get(firstElement));
        }

        if(parse.length > 1){
            String[] elements = parse[1].split(" ");
            for(String element : elements){
                element = element.toLowerCase();
                if(!element.equals(" ") && !element.equals("")){
                    if(defMap.containsKey(element.trim())){
                        list.retainAll(defMap.get(element.trim()));
                    }else{
                        list.clear();
                        break;
                    }
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

                    list.addAll(List.of(split));
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
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------- EDIT ---------------------------------------------\\

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
