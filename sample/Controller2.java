package sample;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Controller2 {

    Dictionary dictionary = new Dictionary();
    int index = -1;

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
            webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(searchedWord));
            removeButton.setDisable(false);
            speaker.setVisible(true);
            dictionary.history.remove(searchedWord);
            dictionary.history.add(searchedWord);
            label.setText(searchedWord);
            edit.setDisable(false);
        }
    }

    //display info when clicked on the listView
    public void displayContent() {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(selectedWord));
        removeButton.setDisable(false);
        speaker.setVisible(true);
        dictionary.history.remove(selectedWord);
        dictionary.history.add(selectedWord);
        label.setText(selectedWord);
        edit.setDisable(false);
    }

    //show a confirmation alert and return res
    public ButtonType alert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Are you sure?");
        Optional<ButtonType> res = alert.showAndWait();

        return res.orElse(ButtonType.CANCEL);
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
        webView.getEngine().loadContent("");
        label.setText("");
        searchWord();
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

    public void back() {
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
    }

    public void changeMode() {
        changeWord.setVisible(true);
    }

    public void exitChange() {
        changeWord.setVisible(false);
    }

    public void saveChange() {
        String newTranslation = change_textArea.getText();
        String currentWord = listView.getSelectionModel().getSelectedItem();
        if (currentWord == null) {
            currentWord = textField.getText();
        }
        if (alert().equals(ButtonType.OK)) {
            dictionary.dictionary.replace(currentWord, newTranslation);
            exitChange();
            webView.getEngine().loadContent("<br/><br/><br/>" + dictionary.dictionary.get(currentWord));
            change_textArea.clear();
        }
    }

    public void add() {
        addNewWord.setVisible(true);
    }

    public void exitAdd() {
        addNewWord.setVisible(false);
    }

    public void saveAdd() {
        String eng = add_english.getText();
        String trans = add_translation.getText();
        if (alert().equals(ButtonType.OK)) {
            dictionary.dictionary.put(eng, trans);
            exitAdd();
            add_english.clear();
            add_translation.clear();
            searchWord();
        }
    }

}
