package org.tick.elp.Service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserDataStorage implements IUserDataStorage {

    public final static String USER_DATA_BASE_NAME = "UserDataBase.db";
    public final static String USER_TEST_DATA_TABLE_NAME = "UserTestDataTable";

    private static UserDataStorage instance = null;
    private ConnectionSource connectionSource;
    private Dao<UserMistake, Integer> userMistakeDao;

    public static UserDataStorage getInstance() {
        if (instance == null) {
            instance = new UserDataStorage();
        }
        return instance;
    }

    @Override
    public void initializeUserDataBase() {
        try {
            String databaseUrl = "jdbc:sqlite:" + USER_DATA_BASE_NAME;
            connectionSource = new JdbcConnectionSource(databaseUrl);
            userMistakeDao = DaoManager.createDao(connectionSource, UserMistake.class);
            TableUtils.createTableIfNotExists(connectionSource, UserMistake.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize user database", e);
        }
    }

    @Override
    public boolean insertUserTestData(List<String> falseAnswers) {
        if (connectionSource == null) {
            initializeUserDataBase();
        }
        try {
            for (String word : falseAnswers) {
                UserMistake mistake = new UserMistake();
                mistake.setWord(word);
                mistake.setMistakeDate(new Date());
                userMistakeDao.create(mistake);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @DatabaseTable(tableName = USER_TEST_DATA_TABLE_NAME)
    public static class UserMistake {
        @DatabaseField(generatedId = true)
        private int id;

        @DatabaseField(columnName = "word")
        private String word;

        @DatabaseField(columnName = "mistake_date")
        private Date mistakeDate;

        public UserMistake() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public Date getMistakeDate() {
            return mistakeDate;
        }

        public void setMistakeDate(Date mistakeDate) {
            this.mistakeDate = mistakeDate;
        }
    }
}
