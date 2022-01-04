package vn.edu.hcmus.fit.pmtoan.Dict_19127586;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * vn.edu.hcmus.fit.pmtoan.Dict_19127586
 * Create by pmtoan
 * Date 1/4/2022 - 8:42 PM
 * Description: ...
 */
public class MapUtils {
    public static void removeDefinitionOutOfMap(String slang, String definition,
                                        Map<String, List<String>> dictionary, Map<String, List<String>> defMap){
        for(String value : definition.split("\\|")){
            value = value.trim();
            dictionary.get(slang).remove(value);


            for(String word : value.split(" ")){
                word = word.trim().toLowerCase();

                if(!checkWordIsContain(word, dictionary.get(slang))){
                    defMap.get(word).remove(slang);
                }
            }
        }
    }

    public static boolean checkWordIsContain(String wordCheck, List<String> listWord){
        for(String wordOfDefinition : listWord){
            String[] arrayWords = wordOfDefinition.split(" ");
            if(Arrays.toString(arrayWords).toLowerCase().contains(wordCheck)){
                return true;
            }
        }
        return false;
    }

    public static void addNewDefinition(String slang, String newDefinition,
                                                Map<String, List<String>> dictionary, Map<String, List<String>> defMap){
        for(String value : newDefinition.split("\\|")){
            value = value.trim();
            dictionary.get(slang).add(value);

            addWordToMap(slang, value, defMap);
        }
    }

    public static void addWordToMap(String slang, String definition, Map<String, List<String>> defMap){
        for(String word : definition.split(" ")){
            List<String> slangList = new ArrayList<>();
            word = word.trim().toLowerCase();

            if(defMap.containsKey(word)){
                if(!defMap.get(word).contains(slang)){
                    defMap.get(word).add(slang);
                }
            } else{
                slangList.add(slang);
                defMap.put(word, slangList);
            }
        }
    }

    public static void addNewSlangToDictionary(String slang, String definition,
                                     Map<String, List<String>> dictionary, Map<String, List<String>> defMap){
        List<String> listDef = new ArrayList<>();
        for(String value : definition.split("\\|")){
            value = value.trim();
            listDef.add(value);

            addWordToMap(slang, value, defMap);
        }

        dictionary.put(slang, listDef);
    }

    public static void addDuplicateSlangToDictionary(String slang, String definition,
                                               Map<String, List<String>> dictionary, Map<String, List<String>> defMap){
        for(String value : definition.split("\\|")){
            value = value.trim();

            addWordToMap(slang, value, defMap);

            dictionary.get(slang).add(value);
        }
    }

    public static void deleteOneSlang(String slang, String definition,
                                                     Map<String, List<String>> dictionary, Map<String, List<String>> defMap){
        removeDefinitionOutOfMap(slang, definition, dictionary, defMap);

        if(dictionary.get(slang).isEmpty()){
            dictionary.remove(slang);
        }
    }
    public static void deleteDuplicateSlang(String slang, Map<String, List<String>> dictionary,
                                            Map<String, List<String>> defMap){
        List<String> listDef = new ArrayList<>();
        listDef.addAll(dictionary.get(slang));

        for(String value : listDef){
            removeDefinitionOutOfMap(slang, value.trim(), dictionary, defMap);
        }

        dictionary.remove(slang);
    }

}
