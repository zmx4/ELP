package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.tick.elp.Entity.TestRecord;
import org.tick.elp.View.MainApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class SummaryController {

    @FXML
    private PieChart reactionTimeChart;

    @FXML
    private LineChart<String, Number> historyChart;

    public void initData(Map<String, Long> reactionTimes, List<TestRecord> history) {
        // Populate PieChart
        for (Map.Entry<String, Long> entry : reactionTimes.entrySet()) {
            reactionTimeChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Populate LineChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("正确率");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        
        for (TestRecord record : history) {
            String dateStr = sdf.format(record.getTestDate());
            series.getData().add(new XYChart.Data<>(dateStr, record.getScore()));
        }
        historyChart.getData().add(series);
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/org/tick/elp/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) reactionTimeChart.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
