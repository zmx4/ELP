package org.tick.elp.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = "TestRecordTable")
public class TestRecord {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "score")
    private double score; // 正确率 0.0 - 1.0

    @DatabaseField(columnName = "test_date")
    private Date testDate;

    @DatabaseField(columnName = "total_questions")
    private int totalQuestions;

    @DatabaseField(columnName = "correct_count")
    private int correctCount;

    public TestRecord() {}

    public TestRecord(double score, Date testDate, int totalQuestions, int correctCount) {
        this.score = score;
        this.testDate = testDate;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public Date getTestDate() { return testDate; }
    public void setTestDate(Date testDate) { this.testDate = testDate; }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }
}
