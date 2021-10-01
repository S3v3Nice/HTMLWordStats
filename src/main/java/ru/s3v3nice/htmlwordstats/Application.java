package ru.s3v3nice.htmlwordstats;

import ru.s3v3nice.htmlwordstats.database.IDatabase;
import ru.s3v3nice.htmlwordstats.html.IHTMLTextTool;
import ru.s3v3nice.htmlwordstats.log.ILog;
import ru.s3v3nice.htmlwordstats.log.Log;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
    private static Application instance;
    private final ILog log;
    private final IHTMLTextTool htmlTextTool;
    private final IDatabase database;

    public Application(ILog log, IHTMLTextTool htmlTextTool, IDatabase database) {
        if (instance == null) {
            instance = this;
        }
        this.log = log;
        this.htmlTextTool = htmlTextTool;
        this.database = database;
    }

    /**
     * @return экземпляр Application
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * Печатает в консоль и сохраняет в базу данных (при возможности)
     * уникальные слова из текста html файла
     *
     * @param htmlPath путь к html файлу
     */
    public void run(String htmlPath) {
        Map<String, Integer> wordStats = htmlTextTool.getWordStats(htmlPath);
        if (wordStats == null) {
            logError("Не удалось прочитать html файл: " + htmlPath);
            return;
        }

        printWordStats(wordStats);
        trySaveWordStats(htmlPath, wordStats);
    }

    /**
     * Печатает в консоль уникальные слова,
     * сортированные по частоте встречаемости
     *
     * @param wordStats словарь вхождений уникальных слов (слово -> количество)
     */
    public void printWordStats(Map<String, Integer> wordStats) {
        List<String> words = new ArrayList<>(wordStats.keySet());
        words.sort((word1, word2) -> wordStats.get(word2).compareTo(wordStats.get(word1)));
        words.forEach((word) -> System.out.println(word + " --- " + wordStats.get(word)));
    }

    /**
     * Пытается сохранить в базу данных результаты поиска уникальных слов
     *
     * @param htmlPath  путь к исходному html файлу
     * @param wordStats словарь вхождений уникальных слов (слово -> количество)
     */
    public void trySaveWordStats(String htmlPath, Map<String, Integer> wordStats) {
        boolean isSavedInDB = database.saveWordStats(htmlPath, wordStats);

        if (isSavedInDB) {
            System.out.println("Результаты успешно сохранены в базу данных.");
        } else {
            logError("Не удалось сохранить результаты в базу данных!");
        }
    }

    /**
     * Логирует ошибку в консоль и лог-файл
     *
     * @param msg сообщение об ошибке
     */
    public void logError(String msg) {
        log.error(msg);
    }

    /**
     * @return путь к директории, в которой запущено приложение
     */
    public static String getDirectoryPath() {
        String path = Log.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        return path.substring(0, path.lastIndexOf("/") + 1);
    }
}
