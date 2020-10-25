package sample;

import javafx.collections.transformation.SortedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class Dictionary {
    Set<String> history = new LinkedHashSet<>();
    /*SortedMap<String,String> dictionary = new TreeMap<String,String>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    });*/

    ArrayList<Word> dictionary = new ArrayList<>();

    Dictionary() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "tomtom169");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tbl_edict");
            while (resultSet.next()) {
                Word newWord = new Word(resultSet.getInt("id"), resultSet.getString("word"), resultSet.getString("detail"));
                dictionary.add(newWord);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /*Dictionary() {
        try {
            File file = new File("C:\\Users\\lapto\\Java\\DictionaryWithGui\\src\\dictionary.txt");
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
    }*/

    int compareWords(String s1, String s2) {
        int length = Math.min(s1.length(), s2.length());
        for (int i = 0; i < length; i++) {
            if (s1.charAt(i) > s2.charAt(i)) {
                return 1;
            }
            if (s1.charAt(i) < s2.charAt(i)) {
                return -1;
            }
        }
        int comp = Integer.compare(s1.length(), s2.length());
        return Integer.compare(comp, 0);
    }

    ArrayList<String> suggestion(String input) {
        ArrayList<String> res = new ArrayList<>();
        for (Word word : dictionary) {
            if (word.eng.length() >= input.length()) {
                if (word.eng.substring(0, input.length()).equals(input)) {
                    res.add(word.eng);
                }
            }
        }
        return res;
    }

    Word findWord(String key) {
        int l = 0, r = dictionary.size() - 1;
        while (l <= r) {
            int m = l + ((r - l) / 2);
            if (dictionary.get(m).eng.equals(key)) {
                return dictionary.get(m);
            }
            if (compareWords(dictionary.get(m).eng, key) < 0) {
                l = m + 1;
            }
            if (compareWords(dictionary.get(m).eng, key) > 0) {
                r = m - 1;
            }
        }
        return null;
    }

    Word findWordbyIterating(String key){
        for(Word word : dictionary){
            if(key.equals(word.eng)){
                return word;
            }
        }
        return null;
    }

}