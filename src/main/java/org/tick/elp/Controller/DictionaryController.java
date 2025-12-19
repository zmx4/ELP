package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.tick.elp.Service.IUserDataStorage;
import org.tick.elp.Service.IWordQueryService;
import org.tick.elp.Service.IWordRandomGet;
import org.tick.elp.Service.QueryService;
import org.tick.elp.Service.SQLWordRandomGet;
import org.tick.elp.Service.UserDataStorage;

import java.util.Map;

public class DictionaryController {
    @FXML
    private TextField wordField;

    @FXML
    private RadioButton queryTranslation;

    @FXML
    private RadioButton queryWord;

    @FXML
    private ToggleGroup searchTypeGroup;

    @FXML
    private TextArea translationArea;

    @FXML
    private ListView<String> resultListView;

    @FXML
    private Label statusLabel;

    @FXML
    private ToggleButton collectionButton;

    @FXML
    private Button detailButton;

    private final IWordQueryService queryService;
    private final IWordRandomGet randomGet;
    private final IUserDataStorage userDataStorage;

    public DictionaryController() {
        this.queryService = new QueryService();
        this.randomGet = new SQLWordRandomGet();
        this.userDataStorage = UserDataStorage.getInstance();
    }

    @FXML
    public void initialize() {
        searchTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (queryTranslation.isSelected()) {
                wordField.setPromptText("输入英文单词，例如 hello");
                collectionButton.setDisable(false);
            } else if (queryWord.isSelected()) {
                wordField.setPromptText("输入中文释义，例如 你好");
                collectionButton.setDisable(true);
                collectionButton.setSelected(false);
            }
        });
        // 初始状态检查
        if (queryWord.isSelected()) {
            collectionButton.setDisable(true);
        }

        resultListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = resultListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    MainController.instance.showDetail(selected, "dictionary");
                }
            }
        });
    }

    @FXML
    protected void onQueryButtonClick() {
        String input = wordField == null ? null : wordField.getText();
        if (input == null || input.isBlank()) {
            setStatus("请输入查询内容");
            return;
        }

        if (queryTranslation.isSelected()) {
            String translation = queryService.queryTranslation(input.trim());
            if (translation == null || translation.isBlank()) {
                translationArea.setText("");
                setStatus("未找到该单词");
                collectionButton.setSelected(false);
                detailButton.setDisable(true);
                return;
            }
            showTranslationArea();
            translationArea.setText(normalizeTranslationForDisplay(translation));
            setStatus("查询成功");
            updateCollectionStatus(input.trim());
            detailButton.setDisable(false);
        } else if (queryWord.isSelected()) {
            java.util.List<String> words = queryService.queryWordByTranslation(input.trim());
            if (words == null || words.isEmpty()) {
                translationArea.setText(""); // Clear just in case, though hidden
                setStatus("未找到对应单词");
                return;
            }
            showResultList();
            resultListView.getItems().setAll(words);
            setStatus("查询成功");
            detailButton.setDisable(true);
        }
    }

    @FXML
    protected void onDetailButtonClick() {
        String word = wordField.getText();
        if (word != null && !word.isBlank()) {
            MainController.instance.showDetail(word.trim(), "dictionary");
        }
    }

    private void showTranslationArea() {
        translationArea.setVisible(true);
        translationArea.setManaged(true);
        resultListView.setVisible(false);
        resultListView.setManaged(false);
    }

    private void showResultList() {
        translationArea.setVisible(false);
        translationArea.setManaged(false);
        resultListView.setVisible(true);
        resultListView.setManaged(true);
    }

    @FXML
    protected void onRandomButtonClick() {
        Map<String, String> result = randomGet.getRandomWordArray(1, "dictionary");
        if (result == null || result.isEmpty()) {
            setStatus("随机取词失败");
            return;
        }

        Map.Entry<String, String> entry = result.entrySet().iterator().next();
        String word = entry.getKey();
        wordField.setText(word);
        
        showTranslationArea();
        translationArea.setText(normalizeTranslationForDisplay(entry.getValue()));
        setStatus("已随机获取一个单词");
        
        if (queryWord.isSelected()) {
             collectionButton.setDisable(false); 
        }
        updateCollectionStatus(word);
        detailButton.setDisable(false);
    }

    @FXML
    protected void onCollectionButtonClick() {
        String word = wordField.getText();
        if (word == null || word.isBlank()) {
            collectionButton.setSelected(false);
            return;
        }
        word = word.trim();
        
        if (collectionButton.isSelected()) {
            if (userDataStorage.addCollection(word)) {
                setStatus("已收藏: " + word);
            } else {
                setStatus("收藏失败");
                collectionButton.setSelected(false);
            }
        } else {
            if (userDataStorage.removeCollection(word)) {
                setStatus("已取消收藏: " + word);
            } else {
                setStatus("取消收藏失败");
                collectionButton.setSelected(true);
            }
        }
    }

    private void updateCollectionStatus(String word) {
        boolean isCollected = userDataStorage.isCollected(word);
        collectionButton.setSelected(isCollected);
    }

    private String normalizeTranslationForDisplay(String translation) {
        if (translation == null) {
            return "";
        }
        return translation.replace("\\n", "\n");
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
