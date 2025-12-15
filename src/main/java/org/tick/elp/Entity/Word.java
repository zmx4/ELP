package org.tick.elp.Entity;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dictionary")
public class Word {
    @DatabaseField(columnName = "word", id = true)
    private String word;
    @DatabaseField(columnName = "translation")
    private String translation;

    public Word() {
    }

    public Word(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
