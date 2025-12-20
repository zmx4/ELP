package org.tick.elp.Service;

import com.j256.ormlite.dao.Dao;
import org.tick.elp.Entity.Word;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryService implements IWordQueryService {
    
    private static final Logger logger = Logger.getLogger(QueryService.class.getName());
    private IDataBaseService dataBaseService;

    public QueryService() {
        this.dataBaseService = DataBaseService.getInstance();
    }

    @Override
    public String queryTranslation(String word) {
        try {
            Dao<Word, String> wordDao = dataBaseService.getWordDao();
            Word result = wordDao.queryForId(word);
            if (result != null) {
                return result.getTranslation();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<String> queryWordByTranslation(String translation) {
        try {
            Dao<Word, String> wordDao = dataBaseService.getWordDao();
            List<Word> results = wordDao.queryBuilder().where().like("translation", "%" + translation + "%").query();
            return results.stream().map(Word::getWord).toList();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public Word queryWord(String word) {
        try {
            Dao<Word, String> wordDao = dataBaseService.getWordDao();
            return wordDao.queryForId(word);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }
    
}
