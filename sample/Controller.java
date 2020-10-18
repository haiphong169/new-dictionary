package sample;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Controller {

    Dictionary dictionary = new Dictionary();
    int index = -1;

    @FXML
    public Label label;

    @FXML
    public Button nextSceneButton;

    @FXML
    public Button backButton;

    @FXML
    public Button addWord;

    @FXML
    public TextArea englishWord;

    @FXML
    public TextArea translation;

    @FXML
    public ImageView speaker;

    @FXML
    public Button removeButton;

    @FXML
    public Button saveButton;

    @FXML
    public TextField textField;

    @FXML
    public ListView<String> listView;

    @FXML
    public TextArea textArea;

    //search when a item in listview get selected
    public void searchWord() {
        String searchedWord = textField.getText().trim();
        if (searchedWord.equals("")) {
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
        }
    }

    public void navigate(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if (keyCode != KeyCode.DOWN && keyCode != KeyCode.UP) {
            index = -1;
        }
        if (keyCode == KeyCode.DOWN) {
            if (index < listView.getItems().size() - 1) {
                index++;
            }
            if (index % 17 == 0) {
                listView.scrollTo(index);
            }
            listView.getSelectionModel().select(index);
            displayContent();

        } else if (keyCode == KeyCode.UP) {
            if (index > 0) {
                index--;
            }
            if (index % 16 == 0) {
                listView.scrollTo(index - 16);
            }
            listView.getSelectionModel().select(index);
            displayContent();
        }
    }

    //search when enter key is pressed
    public void searchWithEnter() {
        String searchedWord = textField.getText().trim();
        if (dictionary.dictionary.containsKey(searchedWord)) {
            textArea.setText("\n\n\n" + dictionary.dictionary.get(searchedWord));
            removeButton.setDisable(false);
            speaker.setVisible(true);
            dictionary.history.remove(searchedWord);
            dictionary.history.add(searchedWord);
            label.setText(searchedWord);
        }
    }

    //display info when clicked on the listView
    public void displayContent() {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        textArea.setText("\n\n\n" + dictionary.dictionary.get(selectedWord));
        removeButton.setDisable(false);
        speaker.setVisible(true);
        dictionary.history.remove(selectedWord);
        dictionary.history.add(selectedWord);
        label.setText(selectedWord);
    }

    //show a confirmation alert and return res
    public ButtonType alert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Are you sure?");
        Optional<ButtonType> res = alert.showAndWait();

        return res.orElse(ButtonType.CANCEL);
    }

    //enable the save button when click on the textarea
    public void change() {
        saveButton.setDisable(false);
    }

    //save changes to the info
    public void save() {
        String newInfo = textArea.getText();
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        if (alert().equals(ButtonType.OK)) {
            dictionary.dictionary.replace(currentWord, newInfo);
        }
        saveButton.setDisable(true);
    }

    public void remove() {
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        if (alert().equals(ButtonType.OK)) {
            dictionary.dictionary.remove(currentWord);
            /*textField.clear();
            textField.requestFocus();*/
        }
        removeButton.setDisable(true);
        textArea.clear();
        label.setText("");
        searchWord();
    }

    public void changeScene(ActionEvent event) throws IOException {
        Parent add_word = FXMLLoader.load(getClass().getResource("addWord.fxml"));
        Scene addNewWord = new Scene(add_word);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addNewWord);
        window.show();
    }

    public void speak() {
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");
        voice.allocate();
        voice.speak(currentWord);
    }

    public void changeBackToDictionary(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene back = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(back);
        window.show();
    }

    public void saveNewWord() {
        String s1 = englishWord.getText();
        String s2 = translation.getText();
        System.out.println(s1);
        System.out.println(s2);
        if (!s1.equals("") && !s2.equals("")) {
            dictionary.dictionary.put(s1, s2);
        }
    }

    public void back(){
        if(dictionary.history.size()>0){
            String lastWord = null;
            for (String value : dictionary.history) {
                lastWord = value;
            }
            dictionary.history.remove(lastWord);
            textField.setText(lastWord);
            textArea.setText("\n\n\n" + dictionary.dictionary.get(lastWord));
            label.setText(lastWord);
            listView.getItems().clear();
            ArrayList<String> arr = new ArrayList<>(dictionary.history);
            Collections.reverse(arr);
            for(String s : arr){
                listView.getItems().add(s);
            }
        }
    }
}
