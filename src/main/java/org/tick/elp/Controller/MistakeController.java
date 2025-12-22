package org.tick.elp.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.tick.elp.Entity.Word;
import org.tick.elp.Service.IUserDataStorage;
import org.tick.elp.Service.IWordQueryService;
import org.tick.elp.Service.QueryService;
import org.tick.elp.Service.UserDataStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MistakeController {

    @FXML
    private ListView<String> mistakeListView;
    @FXML
    private Label statusLabel;
    @FXML
    private VBox listMode;
    @FXML
    private VBox learningMode;
    
    // 学习模式组件
    @FXML
    private Label progressLabel;
    @FXML
    private Label wordLabel;
    @FXML
    private TextArea translationArea;
    @FXML
    private Label hiddenHint;
    @FXML
    private Button showAnswerBtn;
    @FXML
    private Button knownBtn;
    @FXML
    private Button unknownBtn;
    @FXML
    private Label learningStatusLabel;

    private final IUserDataStorage userDataStorage;
    private final IWordQueryService queryService;
    private final ObservableList<String> mistakeList;
    
    // 学习模式状态
    private List<String> learningWords;
    private int currentIndex;
    private int masteredCount;

    public MistakeController() {
        this.userDataStorage = UserDataStorage.getInstance();
        this.queryService = new QueryService();
        this.mistakeList = FXCollections.observableArrayList();
        this.learningWords = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        mistakeListView.setItems(mistakeList);
        mistakeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mistakeListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = mistakeListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    MainController.instance.showDetail(selected, "mistake");
                }
            }
        });
        loadMistakes();
    }

    private void loadMistakes() {
        mistakeList.clear();
        List<String> words = userDataStorage.getMistakes();
        if (words != null && !words.isEmpty()) {
            mistakeList.addAll(words);
            statusLabel.setText("共 " + words.size() + " 个错题单词");
        } else {
            statusLabel.setText("暂无错题记录");
        }
    }

    @FXML
    protected void onRefreshButtonClick() {
        loadMistakes();
    }

    @FXML
    protected void onDeleteButtonClick() {
        String selectedWord = mistakeListView.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            if (userDataStorage.removeMistake(selectedWord)) {
                mistakeList.remove(selectedWord);
                statusLabel.setText("已删除: " + selectedWord);
            }
        } else {
            statusLabel.setText("请先选择要删除的单词");
        }
    }

    @FXML
    protected void onStartLearningClick() {
        if (mistakeList.isEmpty()) {
            statusLabel.setText("没有错题可以学习");
            return;
        }
        
        // 准备学习列表
        learningWords = new ArrayList<>(mistakeList);
        Collections.shuffle(learningWords);
        currentIndex = 0;
        masteredCount = 0;
        
        // 切换到学习模式
        listMode.setVisible(false);
        listMode.setManaged(false);
        learningMode.setVisible(true);
        learningMode.setManaged(true);
        
        showCurrentWord();
    }

    @FXML
    protected void onExitLearningClick() {
        // 切换回列表模式
        learningMode.setVisible(false);
        learningMode.setManaged(false);
        listMode.setVisible(true);
        listMode.setManaged(true);
        
        loadMistakes();
    }

    private void showCurrentWord() {
        if (currentIndex >= learningWords.size()) {
            finishLearning();
            return;
        }
        
        String word = learningWords.get(currentIndex);
        wordLabel.setText(word);
        
        // 查询翻译
        Word wordEntity = queryService.queryWord(word);
        if (wordEntity != null) {
            translationArea.setText(wordEntity.getTranslation());
        } else {
            String translation = queryService.queryTranslation(word);
            translationArea.setText(translation != null ? translation : "未找到翻译");
        }
        
        // 重置状态
        translationArea.setVisible(false);
        hiddenHint.setVisible(true);
        showAnswerBtn.setDisable(false);
        knownBtn.setDisable(true);
        unknownBtn.setDisable(true);
        
        updateProgress();
    }

    private void updateProgress() {
        progressLabel.setText("进度: " + (currentIndex + 1) + "/" + learningWords.size() + 
                " | 已掌握: " + masteredCount);
    }

    @FXML
    protected void onShowAnswerClick() {
        translationArea.setVisible(true);
        hiddenHint.setVisible(false);
        showAnswerBtn.setDisable(true);
        knownBtn.setDisable(false);
        unknownBtn.setDisable(false);
    }

    @FXML
    protected void onKnownClick() {
        String word = learningWords.get(currentIndex);
        
        // 从错题本中移除已掌握的单词
        userDataStorage.removeMistake(word);
        masteredCount++;
        learningStatusLabel.setText("已掌握: " + word);
        learningStatusLabel.setStyle("-fx-text-fill: green;");
        
        currentIndex++;
        showCurrentWord();
    }

    @FXML
    protected void onUnknownClick() {
        String word = learningWords.get(currentIndex);
        learningStatusLabel.setText("继续加油: " + word);
        learningStatusLabel.setStyle("-fx-text-fill: orange;");
        
        // 将该单词移到列表末尾，以便稍后再次学习
        learningWords.add(word);
        
        currentIndex++;
        showCurrentWord();
    }

    private void finishLearning() {
        wordLabel.setText("学习完成！");
        translationArea.setText("");
        translationArea.setVisible(false);
        hiddenHint.setVisible(false);
        showAnswerBtn.setDisable(true);
        knownBtn.setDisable(true);
        unknownBtn.setDisable(true);
        
        learningStatusLabel.setText("本次学习完成！掌握了 " + masteredCount + " 个单词");
        learningStatusLabel.setStyle("-fx-text-fill: green;");
        progressLabel.setText("学习结束");
    }
}
