package org.tick.elp.Service;

import org.tick.elp.Entity.Word;

import java.util.List;

public interface IWordQueryService {
    String queryTranslation(String word);
    List<String> queryWordByTranslation(String translation);
    Word queryWord(String word);
}