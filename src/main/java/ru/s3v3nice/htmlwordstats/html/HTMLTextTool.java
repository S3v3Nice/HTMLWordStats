package ru.s3v3nice.htmlwordstats.html;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HTMLTextTool implements IHTMLTextTool {
    /**
     * Возвращает текст из html файла, отображающийся в браузере
     *
     * @param filePath путь к html файлу
     * @return текст без html тегов и спецсимволов
     * @throws IOException если возникла ошибка при чтении файла
     */
    private String getText(String filePath) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
        boolean isTag = false;
        int ch;

        while ((ch = reader.read()) != -1) {
            char c = (char) ch;

            if (c == '<' || c == '>') {
                isTag = c == '<';
                continue;
            }
            if (!isTag) {
                textBuilder.append(c);
            }
        }

        return textBuilder.toString()
                .replace("&rsquo;", "'")
                .replaceAll("&[^;]*;", " ");
    }

    /**
     * Подсчитывает уникальные слова в тексте html файла
     *
     * @param filePath путь к html файлу
     * @return словарь вхождений (слово -> количество),
     * или null, если возникла ошибка при чтении файла
     */
    public Map<String, Integer> getWordStats(String filePath) {
        String text;

        try {
            text = getText(filePath);
        } catch (IOException e) {
            return null;
        }

        Map<String, Integer> result = new HashMap<>();
        String[] words = text.split("[ ,.!?\";:\\[\\]()\\n\\r\\t/]");

        for (String word : words) {
            if (!word.isEmpty()) {
                result.merge(word.toUpperCase(), 1, Integer::sum);
            }
        }

        return result;
    }
}
