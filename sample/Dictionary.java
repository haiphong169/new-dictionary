package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class Dictionary {
    //Map<String, String> dictionary = new LinkedHashMap<>();
    Set<String> history = new LinkedHashSet<>();
    SortedMap<String,String> dictionary = new TreeMap<String,String>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    });

    Dictionary(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary","root","tomtom169");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tbl_edict");
            while (resultSet.next()){
                String english = resultSet.getString("word");
                String translation = resultSet.getString("detail");
                dictionary.put(english,translation);
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