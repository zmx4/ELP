package org.tick.elp.Service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.tick.elp.Entity.Word;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseService implements IDataBaseService {
    
    private static final Logger logger = Logger.getLogger(DataBaseService.class.getName());
    private static DataBaseService instance = null;
    private ConnectionSource connectionSource;
    private Dao<Word, String> wordDao;

    private DataBaseService() {
    }

    public static DataBaseService getInstance() {
        if (instance == null) {
            instance = new DataBaseService();
        }
        return instance;
    }
    
    @Override
    public void initializeConnect() {
        try {
            String databaseUrl = "jdbc:sqlite:src\\main\\resources\\org\\tick\\elp\\stardict.db";
            connectionSource = new JdbcConnectionSource(databaseUrl);
            wordDao = DaoManager.createDao(connectionSource, Word.class);
            TableUtils.createTableIfNotExists(connectionSource, Word.class);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            throw new RuntimeException("Could not initialize database connection", e);
        }
    }

    @Override
    public Dao<Word, String> getWordDao() {
        if (wordDao == null) {
            initializeConnect();
        }
        return wordDao;
    }
}
