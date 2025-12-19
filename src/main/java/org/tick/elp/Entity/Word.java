package org.tick.elp.Entity;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dictionary")
public class Word {
    @DatabaseField(columnName = "word", id = true)
    private String word;
    @DatabaseField(columnName = "translation")
    private String translation;
    @DatabaseField(columnName = "cet4")
    private int cet4; // 四级
    @DatabaseField(columnName = "cet6")
    private int cet6; // 六级
    @DatabaseField(columnName = "tf")
    private int tf; // 托福
    @DatabaseField(columnName = "ys")
    private int ys;// 雅思
    @DatabaseField(columnName = "hs")
    private int hs; // 高考
    @DatabaseField(columnName = "ph")
    private int ps; //中考

    public int getCet4() {
        return cet4;
    }

    public void setCet4(int cet4) {
        this.cet4 = cet4;
    }

    public int getCet6() {
        return cet6;
    }

    public void setCet6(int cet6) {
        this.cet6 = cet6;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    public int getYs() {
        return ys;
    }

    public void setYs(int ys) {
        this.ys = ys;
    }

    public int getHs() {
        return hs;
    }

    public void setHs(int hs) {
        this.hs = hs;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

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
