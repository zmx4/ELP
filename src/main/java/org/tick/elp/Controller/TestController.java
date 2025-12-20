package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.tick.elp.Service.IWordRandomGet;
import org.tick.elp.Service.SQLWordRandomGet;

import java.util.*;

public class TestController {

    @FXML
    private ComboBox<String> tableSelector;
    @FXML
    private VBox questionArea;
    @FXML
    private Label questionLabel;
    @FXML
    private VBox optionsBox;
    @FXML
    private VBox fillBox;
    @FXML
    private Label hintLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Label resultLabel;
    @FXML
    private Button nextButton;

    private IWordRandomGet wordRandomGet = new SQLWordRandomGet();
    private Map<String, String> wordMap;
    private List<String> wordList;
    private int currentIndex;
    private String currentWord;
    private String currentTranslation;
    private boolean isSelectionMode;
    private boolean isAnswered;

    @FXML
    public void initialize() {
        tableSelector.getItems().addAll("CET4", "CET6", "HighSchool", "PrimarySchool", "tf", "ys");
        tableSelector.getSelectionModel().selectFirst();
        questionArea.setVisible(false);
    }

    @FXML
    public void startTest() {
        String table = tableSelector.getValue();
        // Fetch enough words for questions and distractors
        wordMap = wordRandomGet.getRandomWordArray(20, table);
        if (wordMap == null || wordMap.isEmpty()) {
            resultLabel.setText("无法获取单词，请检查数据库连接或表是否存在。");
            questionArea.setVisible(true);
            optionsBox.setVisible(false);
            fillBox.setVisible(false);
            nextButton.setVisible(false);
            return;
        }
        wordList = new ArrayList<>(wordMap.keySet());
        currentIndex = 0;
        questionArea.setVisible(true);
        nextButton.setVisible(true);
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentIndex >= wordList.size()) {
            questionLabel.setText("测试结束！");
            optionsBox.setVisible(false);
            fillBox.setVisible(false);
            nextButton.setDisable(true);
            resultLabel.setText("");
            return;
        }

        isAnswered = false;
        resultLabel.setText("");
        nextButton.setDisable(true); // Disable next until answered

        currentWord = wordList.get(currentIndex);
        currentTranslation = wordMap.get(currentWord);
        
        // Randomly choose mode: 0 for Selection, 1 for Fill
        isSelectionMode = Math.random() < 0.5;

        if (isSelectionMode) {
            setupSelectionQuestion();
        } else {
            setupFillQuestion();
        }
    }

    private void setupSelectionQuestion() {
        optionsBox.setVisible(true);
        fillBox.setVisible(false);
        questionLabel.setText("请选择正确的翻译: " + currentWord);
        
        optionsBox.getChildren().clear();
        
        List<String> options = new ArrayList<>();
        options.add(currentTranslation);
        
        // Add 3 distractors
        List<String> distractors = new ArrayList<>(wordMap.values());
        distractors.remove(currentTranslation);
        Collections.shuffle(distractors);
        for (int i = 0; i < Math.min(3, distractors.size()); i++) {
            options.add(distractors.get(i));
        }
        Collections.shuffle(options);

        for (String option : options) {
            Button btn = new Button(option);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> checkSelectionAnswer(option));
            optionsBox.getChildren().add(btn);
        }
    }

    private void setupFillQuestion() {
        optionsBox.setVisible(false);
        fillBox.setVisible(true);
        
        // Mask the word
        String masked = maskWord(currentWord);
        questionLabel.setText("补全单词: " + masked);
        hintLabel.setText("翻译: " + currentTranslation);
        inputField.setText("");
        inputField.setDisable(false);
    }
    
    private String maskWord(String word) {
        if (word.length() <= 2) return word;
        char[] chars = word.toCharArray();
        Random random = new Random();
        int maskCount = Math.max(1, word.length() / 3);
        // Ensure we don't mask everything if possible, but random is fine
        for (int i = 0; i < maskCount; i++) {
            int idx = random.nextInt(word.length());
            chars[idx] = '_';
        }
        return new String(chars);
    }

    private void checkSelectionAnswer(String selected) {
        if (isAnswered) return;
        isAnswered = true;
        
        if (selected.equals(currentTranslation)) {
            resultLabel.setText("正确!");
            resultLabel.setStyle("-fx-text-fill: green;");
        } else {
            resultLabel.setText("错误! 正确答案: " + currentTranslation);
            resultLabel.setStyle("-fx-text-fill: red;");
        }
        disableOptions();
        nextButton.setDisable(false);
    }
    
    private void disableOptions() {
         optionsBox.getChildren().forEach(node -> node.setDisable(true));
    }

    @FXML
    public void checkFillAnswer() {
        if (isAnswered) return;
        
        String input = inputField.getText().trim();
        if (input.isEmpty()) return;

        isAnswered = true;
        if (input.equalsIgnoreCase(currentWord)) {
            resultLabel.setText("正确!");
            resultLabel.setStyle("-fx-text-fill: green;");
        } else {
            resultLabel.setText("错误! 正确答案: " + currentWord);
            resultLabel.setStyle("-fx-text-fill: red;");
        }
        inputField.setDisable(true);
        nextButton.setDisable(false);
    }

    @FXML
    public void onNext() {
        currentIndex++;
        nextQuestion();
    }
}
