package org.tick.elp.Service;

import com.j256.ormlite.dao.Dao;
import org.tick.elp.Entity.Word;

public interface IDataBaseService {
    void initializeConnect();
    Dao<Word, String> getWordDao();
    
}
