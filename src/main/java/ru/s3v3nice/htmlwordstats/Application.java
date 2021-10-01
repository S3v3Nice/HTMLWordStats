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
     * Логирует ошибку в консоль и лог-файл
     *
     * @param msg сообщение об ошибке
     */
    public void logError(String msg) {
        log.error(msg);
    }

    /**
     * Печатает в консоль уникальные слова в тексте html файла,
     * сортированные по частоте встречаемости, а также сохраняет
     * результаты в базу данных
     *
     * @param filePath путь к html файлу
     */
    public void printWordStats(String filePath) {
        Map<String, Integer> wordStats = htmlTextTool.getWordStats(filePath);
        if (wordStats == null) {
            logError("Ошибка при чтении html файла: " + filePath);
            return;
        }

        List<String> words = new ArrayList<>(wordStats.keySet());
        words.sort((word1, word2) -> wordStats.get(word2).compareTo(wordStats.get(word1)));
        words.forEach((word) -> System.out.println(word + " --- " + wordStats.get(word)));

        database.saveWordStats(filePath, wordStats);
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
