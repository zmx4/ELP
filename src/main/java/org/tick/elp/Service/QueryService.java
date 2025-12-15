package org.tick.elp.Service;

import com.j256.ormlite.dao.Dao;
import org.tick.elp.Entity.Word;

import java.sql.SQLException;

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
    
}
