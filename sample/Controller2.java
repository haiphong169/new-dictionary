package sample;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static sample.GoogleTransApi.*;


public class Controller2 {

    Dictionary dictionary = new Dictionary();

    @FXML
    public ImageView api_speaker;

    @FXML
    public Button changeToAPI;

    @FXML
    public AnchorPane api;

    @FXML
    public Button api_back;

    @FXML
    public TextArea api_eng;

    @FXML
    public TextArea api_trans;

    @FXML
    public AnchorPane addNewWord;

    @FXML
    public TextArea add_english;

    @FXML
    public TextArea add_translation;

    @FXML
    public Button add_back;

    @FXML
    public Button add_save;

    @FXML
    public Button edit;

    @FXML
    public AnchorPane changeWord;

    @FXML
    public TextArea change_textArea;

    @FXML
    public Button change_backButton;

    @FXML
    public Button change_saveButton;

    @FXML
    public WebView webView;

    @FXML
    public Label label;

    @FXML
    public Button addWord;

    @FXML
    public ImageView speaker;

    @FXML
    public Button removeButton;

    @FXML
    public TextField textField;

    @FXML
    public ListView<String> listView;

    //update listview
    public void searchWord() {
        String searchedWord = textField.getText().trim();
        listView.getItems().clear();
        ArrayList<String> suggestion = dictionary.suggestion(searchedWord);
        for (String word : suggestion) {
            listView.getItems().add(word);
        }
        /*if (searchedWord.equals("")) {
            listView.getItems().clear();
            ArrayList<String> arr_searchedWord = new ArrayList<>(dictionary.history);
            Collections.reverse(arr_searchedWord);
            for (String s : arr_searchedWord) {
                listView.getItems().add(s);
            }
        } else {
            if (dictionary.searchedWords(searchedWord).isEmpty()) {
                return;
            }
            listView.getItems().clear();
            ArrayList<String> arr = dictionary.searchedWords(searchedWord);
            for (String word : arr) {
                listView.getItems().add(word);
            }
        }*/
    }

    public void navigate(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if (keyCode == KeyCode.DOWN) {
            listView.requestFocus();
            listView.getSelectionModel().select(0);
        }
    }

    //search when enter key is pressed
    public void searchWithEnter() throws SQLException, ClassNotFoundException {
        String searchedWord = textField.getText().trim();
        if (dictionary.findWord(searchedWord) != null) {
            Word res = dictionary.findWord(searchedWord);
            webView.getEngine().loadContent("<br/><br/><br/>" + res.translation);
            removeButton.setDisable(false);
            speaker.setVisible(true);
            label.setText(searchedWord);
            edit.setDisable(false);
        } else {
            if(dictionary.findWordbyIterating(searchedWord)!=null){
                Word res = dictionary.findWordbyIterating(searchedWord);
                webView.getEngine().loadContent("<br/><br/><br/>" + res.translation);
                removeButton.setDisable(false);
                speaker.setVisible(true);
                label.setText(searchedWord);
                edit.setDisable(false);
            }
            else{
                label.setText("");
                webView.getEngine().loadContent("Không có!");
            }
        }
        /*if (dictionary.dictionary.containsKey(searchedWord)) {
            webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(searchedWord));
            removeButton.setDisable(false);
            speaker.setVisible(true);
            dictionary.history.remove(searchedWord);
            dictionary.history.add(searchedWord);
            label.setText(searchedWord);
            edit.setDisable(false);
        } else {
            label.setText("");
            webView.getEngine().loadContent("Không có!");
        }*/
    }

    //display info when clicked on the listView
    public void displayContentWithKey(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if (listView.getSelectionModel().getSelectedIndex() == 0 && keyCode == KeyCode.UP) {
            textField.requestFocus();
        }
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        if (dictionary.findWord(selectedWord) != null) {
            Word res = dictionary.findWord(selectedWord);
            webView.getEngine().loadContent("<br/><br/><br/>" + res.translation);
            removeButton.setDisable(false);
            speaker.setVisible(true);
            /*dictionary.history.remove(selectedWord);
            dictionary.history.add(selectedWord);*/
            label.setText(selectedWord);
            edit.setDisable(false);
        } else {
            if(dictionary.findWordbyIterating(selectedWord)!=null){
                Word res = dictionary.findWordbyIterating(selectedWord);
                webView.getEngine().loadContent("<br/><br/><br/>" + res.translation);
                removeButton.setDisable(false);
                speaker.setVisible(true);
                /*dictionary.history.remove(selectedWord);
                dictionary.history.add(selectedWord);*/
                label.setText(selectedWord);
                edit.setDisable(false);
            }
            else{
                label.setText("");
                webView.getEngine().loadContent("Không có!");
            }
        }
    }

    public void displayContent() {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        if (dictionary.findWord(selectedWord) != null) {
            Word res = dictionary.findWord(selectedWord);
            webView.getEngine().loadContent("<br/><br/><br/>" + res.translation);
            removeButton.setDisable(false);
            speaker.setVisible(true);
            /*dictionary.history.remove(selectedWord);
            dictionary.history.add(selectedWord);*/
            label.setText(selectedWord);
            edit.setDisable(false);
        } else {
            if (dictionary.findWordbyIterating(selectedWord)==null) {
                label.setText("");
                webView.getEngine().loadContent("Không có!");
            } else {
                Word res = dictionary.findWordbyIterating(selectedWord);
                webView.getEngine().loadContent("<br/><br/><br/>" + res.translation);
                removeButton.setDisable(false);
                speaker.setVisible(true);
                /*dictionary.history.remove(selectedWord);
                dictionary.history.add(selectedWord);*/
                label.setText(selectedWord);
                edit.setDisable(false);
            }
        }
    }

    //show a confirmation alert and return res
    public ButtonType alert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Are you sure?");
        Optional<ButtonType> res = alert.showAndWait();

        return res.orElse(ButtonType.CANCEL);
    }

    public ButtonType alertAdd() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("The dictionary already contained this word. Want to replace instead?");
        Optional<ButtonType> res = alert.showAndWait();

        return res.orElse(ButtonType.CANCEL);
    }

    public void remove() throws ClassNotFoundException, SQLException {
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        Word w = dictionary.findWord(currentWord);
        if(w == null){
            w = dictionary.findWordbyIterating(currentWord);
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "tomtom169");
        Statement statement = connection.createStatement();
        if (alert().equals(ButtonType.OK)) {
            statement.execute("DELETE FROM `dictionary`.`tbl_edict` WHERE (`id` = '" + w.id + "');");
            dictionary.dictionary.remove(w);
            /*textField.clear();
            textField.requestFocus();*/
            removeButton.setDisable(true);
            edit.setDisable(true);
            webView.getEngine().loadContent("");
            label.setText("");
            searchWord();
        }
        /*if (alert().equals(ButtonType.OK)) {
            dictionary.dictionary.remove(currentWord);
            textField.clear();
            textField.requestFocus();
            removeButton.setDisable(true);
            edit.setDisable(true);
            webView.getEngine().loadContent("");
            label.setText("");
            searchWord();
        }*/
    }

    /*public void speak() {
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");
        voice.allocate();
        voice.speak(currentWord);
    }*/

    public void speak() {
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        Speak(currentWord);
    }

    /*public void back() {
        if (dictionary.history.size() > 0) {
            String lastWord = null;
            for (String value : dictionary.history) {
                lastWord = value;
            }
            dictionary.history.remove(lastWord);
            textField.setText(lastWord);
            webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(lastWord));
            label.setText(lastWord);
            listView.getItems().clear();
            ArrayList<String> arr = new ArrayList<>(dictionary.history);
            Collections.reverse(arr);
            for (String s : arr) {
                listView.getItems().add(s);
            }
        }
    }*/

    public void changeMode() {
        changeWord.setVisible(true);
    }

    public void exitChange() {
        changeWord.setVisible(false);
    }

    public void saveChange() throws SQLException, ClassNotFoundException {
        String newTranslation = change_textArea.getText();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "tomtom169");
        Statement statement = connection.createStatement();
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        Word w = dictionary.findWord(currentWord);
        if(w == null){
            w = dictionary.findWordbyIterating(currentWord);
        }
        if (alert().equals(ButtonType.OK)) {
            statement.execute("UPDATE `dictionary`.`tbl_edict` SET `detail` = '" + newTranslation + "' WHERE (`id` = '" + w.id + "');");
            dictionary.dictionary.get(w.id - 1).translation = newTranslation;
            exitChange();
            webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(w.id - 1).translation);
            change_textArea.clear();
            /*dictionary.dictionary.replace(currentWord, newTranslation);
            exitChange();
            webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(currentWord));
            change_textArea.clear();*/
        }
    }

    public void add() {
        addNewWord.setVisible(true);
    }

    public void exitAdd() {
        addNewWord.setVisible(false);
    }

    public void saveAdd() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "tomtom169");
        Statement statement = connection.createStatement();
        String eng = add_english.getText();
        String trans = add_translation.getText();
        if ((dictionary.findWord(eng) == null) && (dictionary.findWordbyIterating(eng) == null)) {
            if (alert().equals(ButtonType.OK)) {
                statement.execute("INSERT INTO `dictionary`.`tbl_edict` (`word`, `detail`) VALUES ('" + eng + "', '" + trans + "');");
                Word newWord = new Word(dictionary.dictionary.size(),eng,trans);
                dictionary.dictionary.add(newWord);
                exitAdd();
                add_english.clear();
                add_translation.clear();
                searchWord();
            }
        }
        else {
            if (alertAdd().equals(ButtonType.OK)) {
                Word res = dictionary.findWord(eng);
                if(res == null){
                    res = dictionary.findWordbyIterating(eng);
                }
                statement.execute("UPDATE `dictionary`.`tbl_edict` SET `detail` = '" + trans + "' WHERE (`id` = '" + res.id + "');");
                dictionary.dictionary.get(res.id - 1).translation = trans;
                exitAdd();
                add_english.clear();
                add_translation.clear();
                searchWord();
            }
        }
        /*if (!dictionary.dictionary.containsKey(eng)) {
            if (alert().equals(ButtonType.OK)) {
                dictionary.dictionary.put(eng, trans);
                exitAdd();
                add_english.clear();
                add_translation.clear();
                searchWord();
            }
        } else {
            if (alertAdd().equals(ButtonType.OK)) {
                dictionary.dictionary.replace(eng, trans);
                exitAdd();
                add_english.clear();
                add_translation.clear();
                searchWord();
            }
        }*/
    }

    public void realtimeTrans(KeyEvent keyEvent) {
        String eng = api_eng.getText();
        api_trans.setText(translate(eng));
        api_speaker.setVisible(true);
        /*KeyCode keyCode = keyEvent.getCode();
        if(keyCode == KeyCode.UP){
            String eng = api_eng.getText();
            api_trans.setText(translate(eng));
            api_speaker.setVisible(true);
        }*/
    }

    public void changeToAPI() {
        api.setVisible(true);
    }

    public void backFromAPI() {
        api_eng.clear();
        api_trans.clear();
        api.setVisible(false);
    }

    public void apiSpeak() {
        Speak(api_eng.getText());
    }

}
