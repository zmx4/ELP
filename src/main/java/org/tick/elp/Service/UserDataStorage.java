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
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDataStorage implements IUserDataStorage {

    private static final Logger logger = Logger.getLogger(UserDataStorage.class.getName());

    public final static String USER_DATA_BASE_NAME = "UserDataBase.db";
    public final static String USER_TEST_DATA_TABLE_NAME = "UserTestDataTable";
    public final static String USER_COLLECTION_TABLE_NAME = "UserCollectionTable";

    private static UserDataStorage instance = null;
    private ConnectionSource connectionSource;
    private Dao<UserMistake, Integer> userMistakeDao;
    private Dao<UserCollection, Integer> userCollectionDao;

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
            userCollectionDao = DaoManager.createDao(connectionSource, UserCollection.class);
            TableUtils.createTableIfNotExists(connectionSource, UserMistake.class);
            TableUtils.createTableIfNotExists(connectionSource, UserCollection.class);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
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
            logger.log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public boolean addCollection(String word) {
        if (connectionSource == null) initializeUserDataBase();
        try {
            if (isCollected(word)) return true;
            UserCollection collection = new UserCollection();
            collection.setWord(word);
            collection.setCollectionDate(new Date());
            userCollectionDao.create(collection);
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public boolean removeCollection(String word) {
        if (connectionSource == null) initializeUserDataBase();
        try {
            List<UserCollection> list = userCollectionDao.queryForEq("word", word);
            if (list != null && !list.isEmpty()) {
                userCollectionDao.delete(list);
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public List<String> getCollections() {
        if (connectionSource == null) initializeUserDataBase();
        try {
            List<UserCollection> list = userCollectionDao.queryForAll();
            return list.stream().map(UserCollection::getWord).toList();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            return List.of();
        }
    }

    @Override
    public boolean isCollected(String word) {
        if (connectionSource == null) initializeUserDataBase();
        try {
            List<UserCollection> list = userCollectionDao.queryForEq("word", word);
            return list != null && !list.isEmpty();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            return false;
        }
    }

    // @Override
    // public boolean clearUserTestData() {
    //     if (connectionSource == null) initializeUserDataBase();
    //     try {
    //         TableUtils.clearTable(connectionSource, UserMistake.class);
    //         return true;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

    @DatabaseTable(tableName = USER_COLLECTION_TABLE_NAME)
    public static class UserCollection {
        @DatabaseField(generatedId = true)
        private int id;

        @DatabaseField(columnName = "word")
        private String word;

        @DatabaseField(columnName = "collection_date")
        private Date collectionDate;

        public UserCollection() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }
        public Date getCollectionDate() { return collectionDate; }
        public void setCollectionDate(Date collectionDate) { this.collectionDate = collectionDate; }
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

    @Override
    public boolean clearUserTestData() {
        if (connectionSource == null) {
            initializeUserDataBase();
        }
        try {
            // TableUtils.clearTable(connectionSource, UserMistake.class);
            //断开连接 删除数据库文件
            connectionSource.close();
            java.io.File dbFile = new java.io.File(USER_DATA_BASE_NAME);
            if (dbFile.exists()) {
                dbFile.delete();
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            return false;
        }
    }
}
