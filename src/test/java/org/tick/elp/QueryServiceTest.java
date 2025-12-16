package org.tick.elp;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tick.elp.Entity.Word;
import org.tick.elp.Service.DataBaseService;
import org.tick.elp.Service.QueryService;

import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class QueryServiceTest {
    private QueryService queryService;

    @Test
    public void testQueryTranslation() throws SQLException {
        // Ensure data exists
        DataBaseService.getInstance().getWordDao().createOrUpdate(new Word("hello", "你好"));

        queryService = new QueryService();
        String translation = queryService.queryTranslation("hello");
        System.out.println("Translation of 'hello': " + translation);
        assert translation != null;
    }

    @Test
    public void testQueryWordByTranslation() throws SQLException {
        // Ensure data exists
        DataBaseService.getInstance().getWordDao().createOrUpdate(new Word("world", "世界"));

        queryService = new QueryService();
        var words = queryService.queryWordByTranslation("世界");
        System.out.println("Words with translation '世界': " + words);
        assert words != null && !words.isEmpty();
    }
}
