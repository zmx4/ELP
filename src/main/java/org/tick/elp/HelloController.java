package org.tick.elp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.tick.elp.Service.IWordQueryService;
import org.tick.elp.Service.IWordRandomGet;
import org.tick.elp.Service.QueryService;
import org.tick.elp.Service.SQLWordRandomGet;

import java.util.Map;

public class HelloController {
    @FXML
    private TextField wordField;

    @FXML
    private TextArea translationArea;

    @FXML
    private Label statusLabel;

    private final IWordQueryService queryService;
    private final IWordRandomGet randomGet;

    public HelloController() {
        this.queryService = new QueryService();
        this.randomGet = new SQLWordRandomGet();
    }

    @FXML
    protected void onQueryButtonClick() {
        String word = wordField == null ? null : wordField.getText();
        if (word == null || word.isBlank()) {
            setStatus("请输入要查询的单词");
            return;
        }

        String translation = queryService.queryTranslation(word.trim());
        if (translation == null || translation.isBlank()) {
            translationArea.setText("");
            setStatus("未找到该单词");
            return;
        }

        translationArea.setText(translation);
        setStatus("查询成功");
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
        translationArea.setText(entry.getValue() == null ? "" : entry.getValue());
        setStatus("已随机获取一个单词");
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
