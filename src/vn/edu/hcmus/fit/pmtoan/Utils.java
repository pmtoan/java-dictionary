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
    public static void readFile(){
        File file = new File("slang.txt");

        try {
            Map<String, String> map = new HashMap<>();
            List<String> lines = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
                //String[] elements = line.split("`");
                //System.out.println(line);
                //map.put(elements[0],  elements[1]);
            }
            System.out.println(lines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        readFile();
    }
}
