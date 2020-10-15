package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dictionary {
    //Map<String, String> dictionary = new LinkedHashMap<>();
    SortedMap<String,String> dictionary = new TreeMap<String,String>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    });

    Dictionary() {
        try {
            File file = new File("C:\\Users\\lapto\\Java\\TestSceneBuilder\\src\\dictionary.txt");
            Scanner scan = new Scanner(file);
            String english = "";
            String explaination = "";
            while (scan.hasNextLine()) {
                String aWordReadFromFile = scan.next();
                String aLineReadFromFile;
                if (aWordReadFromFile.charAt(0) == '@') {
                    int lengthWord = aWordReadFromFile.length();
                    aLineReadFromFile = aWordReadFromFile.substring(1, lengthWord)
                            + scan.nextLine();
                } else {
                    aLineReadFromFile = aWordReadFromFile + scan.nextLine();
                }

                if (aWordReadFromFile.charAt(0) == '@') {
                    if (english != "" && explaination != "") {
                        dictionary.put(english, explaination);
                    }
                    english = aLineReadFromFile.substring(0, aLineReadFromFile.indexOf(" /") + 1);
                    english = english.trim();
                    explaination = "";
                }
                explaination += aLineReadFromFile;
                explaination += "\n";
            }
            dictionary.put(english, explaination);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> searchedWords(String input) {
        ArrayList<String> res = new ArrayList<>();
        for (String word : dictionary.keySet()) {
            if (word.length() >= input.length()) {
                if (word.substring(0, input.length()).equals(input)) {
                    res.add(word);
                }
            }
        }
        return res;
    }
}