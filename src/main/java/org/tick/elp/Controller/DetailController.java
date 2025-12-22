package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.tick.elp.Entity.Word;
import org.tick.elp.Service.IWordQueryService;
import org.tick.elp.Service.QueryService;

public class DetailController {

    @FXML
    private Label wordLabel;

    @FXML
    private HBox tagsBox;

    @FXML
    private TextArea translationArea;

    private final IWordQueryService queryService;
    private String fromView;

    public DetailController() {
        this.queryService = new QueryService();
    }

    public void initData(String wordStr, String fromView) {
        this.fromView = fromView;
        Word word = queryService.queryWord(wordStr);
        if (word != null) {
            wordLabel.setText(word.getWord());
            translationArea.setText(word.getTranslation());
            
            tagsBox.getChildren().clear();
            addTagIfPresent("CET4", word.getCet4());
            addTagIfPresent("CET6", word.getCet6());
            addTagIfPresent("TOEFL", word.getTf());
            addTagIfPresent("IELTS", word.getYs());
            addTagIfPresent("高考", word.getHs());
            addTagIfPresent("中考", word.getPs());
        } else {
            wordLabel.setText(wordStr);
            translationArea.setText("未找到详细信息");
        }
    }

    private void addTagIfPresent(String tagName, int value) {
        if (value > 0) {
            Label tag = new Label(tagName);
            tag.getStyleClass().add("tag");
            tagsBox.getChildren().add(tag);
        }
    }

    @FXML
    protected void onBackButtonClick() {
        if ("collection".equals(fromView)) {
            MainController.instance.showCollection();
        } else {
            MainController.instance.showDictionary();
        }
    }
}
