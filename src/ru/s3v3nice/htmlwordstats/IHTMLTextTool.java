package ru.s3v3nice.htmlwordstats;

import java.util.Map;

public interface IHTMLTextTool {
    /**
     * Подсчитывает уникальные слова в тексте html файла
     *
     * @param filePath путь к html файлу
     * @return словарь вхождений (слово -> количество),
     * или null, если возникла ошибка при чтении файла
     */
    Map<String, Integer> getWordStats(String filePath);
}
