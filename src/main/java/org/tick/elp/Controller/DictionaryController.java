package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label statusLabel;

    @FXML
    private ToggleButton collectionButton;

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
                return;
            }
            translationArea.setText(normalizeTranslationForDisplay(translation));
            setStatus("查询成功");
            updateCollectionStatus(input.trim());
        } else if (queryWord.isSelected()) {
            java.util.List<String> words = queryService.queryWordByTranslation(input.trim());
            if (words == null || words.isEmpty()) {
                translationArea.setText("");
                setStatus("未找到对应单词");
                return;
            }
            translationArea.setText(String.join("\n", words));
            setStatus("查询成功");
        }
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
        translationArea.setText(normalizeTranslationForDisplay(entry.getValue()));
        setStatus("已随机获取一个单词");
        
        // 随机取词后，确保切换到“搜翻译”模式或者至少让收藏按钮可用
        // 这里简单处理：如果当前是搜单词模式，可能需要切换UI状态，或者仅仅启用收藏按钮
        // 为了一致性，随机取词通常是取英文单词，所以我们可以认为它是英文
        if (queryWord.isSelected()) {
             // 如果在搜单词模式下随机取词，wordField被填入了英文，这时候应该允许收藏
             // 但为了避免混淆，最好切换回搜翻译模式，或者临时允许
             // 简单起见，我们只更新状态，用户如果想收藏，可能需要手动切换模式或者我们在这里允许
             // 让我们保持简单：随机取词总是填充英文单词，所以我们检查该单词是否收藏
             collectionButton.setDisable(false); 
        }
        updateCollectionStatus(word);
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
