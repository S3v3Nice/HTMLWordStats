package ru.s3v3nice.htmlwordstats.database;

import java.util.Map;

public interface IDatabase {
    /**
     * Сохраняет в базу данных результаты поиска уникальных слов в тексте
     * вместе с путем к исходному html файлу
     *
     * @param filePath  путь к html файлу
     * @param wordStats словарь вхождений уникальных слов (слово -> количество)
     * @return true, если получилось сохранить, или false, если не получилось
     */
    boolean saveWordStats(String filePath, Map<String, Integer> wordStats);
}
