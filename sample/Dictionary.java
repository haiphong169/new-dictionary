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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/luctq","root","luc@tq610");
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
            String filePath = new File("").getAbsolutePath();
            File file = new File(filePath + "\\dictionary.txt");
            Scanner scan = new Scanner(file);
            String english = "";
            String explaintion = "";
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
                    if (english != "" && explaintion != "") {
                        dictionary.put(english, explaintion);
                    }
                    if (aLineReadFromFile.indexOf(" /") > 0) {
                        english = aLineReadFromFile.substring(0, aLineReadFromFile.indexOf(" /") + 1);
                        english = english.trim();
                        explaintion = "";
                    } else {
                        english = aLineReadFromFile.substring(0, aLineReadFromFile.length());
                        explaintion = "";
                    }
                }
                explaintion += aLineReadFromFile;
                explaintion += "\n";
            }
            dictionary.put(english, explaintion);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    } */

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
    // hàm để đếm số kí tự giống nhau giữa từ đúng và từ sai. VD: have và heve hàm này trả về 3
    int raitoSame(String word, String wrongWord) {
        // ta chỉ xét trong khoảng từ trong khoảng như ở dưới. VD từ nhập sai là have có độ dài là 4 thì ta chỉ xét từ trong từ điển có length
        // từ 2 đến 6.
        if (word.length() < wrongWord.length()- 2|| word.length() > wrongWord.length() + 2) {
            return 0;
        }
        // dòng này để lọc các từ trong từ điển có kí tự đầu giống với từ nhập sai. VD heve thì ta chỉ xét các từ trong từ điển có kí tự đầu là h.
        // thực ra dòng này bỏ đi cũng được nhưng nó ra hơi nhiều từ nên t co khoảng lại cho dễ.
        if (word.charAt(0) != wrongWord.charAt(0)) return 0;
        int i = 0;
        int j = 0;
        int count = 0;
        //đếm các từ giống nhau từ trái sang phải.VD have với heve thì ta thu được 1 kí tự giống nhau là h.
        while (i < word.length() && j < wrongWord.length() && word.charAt(i) == wrongWord.charAt(j)) {
            i++;
            j++;
            count++;
        }

        i = word.length()-1;
        j = wrongWord.length()-1;
        //đếm các từ giống nhau từ phải sang trái. với ví dụ trên thì có 2 kí tự giống nhau là v,e.
        while (i > 0 && j > 0 && word.charAt(i) == wrongWord.charAt(j)) {
            i--;
            j--;
            count++;
        }
        //tổng 2 lần là ra số kí tự giống nhau.
        return count;
    }
   ArrayList<String> wrongSearch(String input) {
        int limit = input.length()/4;
        ArrayList<String> res = new ArrayList<>();
        for (String word : dictionary.keySet()) {
            if (raitoSame(word, input) >= input.length()-limit) {
                    res.add(word);
            }
        }
        return res;
    }
    /*ArrayList<String> worngSearch(String input) {
        ArrayList<String> res = new ArrayList<>();
        int[] count = new int[dictionary.size()];
        Arrays.fill(count, 0);
        int k = 0;
        int max = 0;
        for (String word : dictionary.keySet()) {
            
            if (word.charAt(0) == input.charAt(0)) {
                if (word.length() == input.length()) {
                    for (int i = 0; i < word.length(); i++) {
                        if (input.charAt(i) == word.charAt(i)) {
                            count[k]++;
                        }
                    }
                    if (count[k] > max) max = count[k];
                    k++;
                } else if (word.length() > input.length()) {
                    if (word.length() < input.length() + 3) {
                        int j = 0;
                        for (int i = 0; i < word.length(); i++) {
                            if (j >= input.length()) break;
                            if (input.charAt(j) == word.charAt(i)) {
                                count[k]++;
                                j++;
                            }
                        }
                        if (count[k] > max) count[k] = max;
                        k++;
                    } else k++;

                } else {
                    if (word.length() > input.length()-2) {
                        int j = 0;
                        for (int i = 0; i < input.length(); i++) {
                            if (j >= word.length()) break;
                            if (word.charAt(j) == input.charAt(i)) {
                                count[k]++;
                                j++;
                            }
                        }
                        if (count[k] > max) count[k] = max;
                        k++;
                    } else k++;

                }
            } else k++;
        }
        k = 0;
        System.out.println(max);
        for (String word : dictionary.keySet()) {
            if (count[k++] == max) {
                res.add(word);
            }
        }

        return res;
    }*/

}