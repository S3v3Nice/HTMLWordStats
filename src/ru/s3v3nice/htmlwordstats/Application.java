package ru.s3v3nice.htmlwordstats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
    private static Application instance;
    private final ILog log;
    private final IHTMLTextTool htmlTextTool;

    public Application(ILog log, IHTMLTextTool htmlTextTool) {
        this.log = log;
        this.htmlTextTool = htmlTextTool;
        if (instance == null) {
            instance = this;
        }
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
     * сортированные по частоте встречаемости
     *
     * @param filePath путь к html файлу
     */
    public void printWordStats(String filePath) {
        Map<String, Integer> map = htmlTextTool.getWordStats(filePath);
        if (map == null) {
            logError("Ошибка при чтении html файла: " + filePath);
            return;
        }

        List<String> words = new ArrayList<>(map.keySet());
        words.sort((word1, word2) -> map.get(word2).compareTo(map.get(word1)));

        words.forEach((word) -> System.out.println(word + " --- " + map.get(word)));
    }

    /**
     * @return путь к директории, в которой запущено приложение
     */
    public static String getApplicationPath() {
        String path = Log.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        return path.substring(0, path.lastIndexOf("/") + 1);
    }
}
