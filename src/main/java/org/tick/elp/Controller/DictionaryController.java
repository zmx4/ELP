package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.tick.elp.Service.IWordQueryService;
import org.tick.elp.Service.IWordRandomGet;
import org.tick.elp.Service.QueryService;
import org.tick.elp.Service.SQLWordRandomGet;

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

    private final IWordQueryService queryService;
    private final IWordRandomGet randomGet;

    public DictionaryController() {
        this.queryService = new QueryService();
        this.randomGet = new SQLWordRandomGet();
    }

    @FXML
    public void initialize() {
        searchTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (queryTranslation.isSelected()) {
                wordField.setPromptText("输入英文单词，例如 hello");
            } else if (queryWord.isSelected()) {
                wordField.setPromptText("输入中文释义，例如 你好");
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
                return;
            }
            translationArea.setText(normalizeTranslationForDisplay(translation));
            setStatus("查询成功");
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
        wordField.setText(entry.getKey());
        translationArea.setText(normalizeTranslationForDisplay(entry.getValue()));
        setStatus("已随机获取一个单词");
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
