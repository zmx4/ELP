package org.tick.elp.Service;

import com.j256.ormlite.dao.Dao;
import org.tick.elp.Entity.Word;

import java.sql.SQLException;
import java.util.List;

public class QueryService implements IWordQueryService {
    
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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> queryWordByTranslation(String translation) {
        try {
            Dao<Word, String> wordDao = dataBaseService.getWordDao();
            List<Word> results = wordDao.queryForEq("translation", translation);
            return results.stream().map(Word::getWord).toList();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
