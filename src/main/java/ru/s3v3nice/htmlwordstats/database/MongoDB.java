package ru.s3v3nice.htmlwordstats.database;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDB implements IDatabase {
    private final MongoDatabase database;

    public MongoDB(String databaseName) {
        this(databaseName, "127.0.0.1", 27017);
    }

    public MongoDB(String databaseName, String host, int port) {
        this(databaseName, host, port, "", "");
    }

    public MongoDB(String databaseName, String host, int port, String user, String password) {
        StringBuilder uri = new StringBuilder("mongodb://");

        if (!user.isEmpty() && !password.isEmpty()) {
            uri.append(user).append(":").append(password).append("@");
        }
        uri.append(host).append(":").append(port);

        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        database = MongoClients.create(uri.toString()).getDatabase(databaseName);
    }

    /**
     * Сохраняет в базу данных результаты поиска уникальных слов в тексте
     * вместе с путем к исходному html файлу
     *
     * @param filePath  путь к html файлу
     * @param wordStats словарь вхождений уникальных слов (слово -> количество)
     * @return true, если получилось сохранить, или false, если не получилось
     */
    public boolean saveWordStats(String filePath, Map<String, Integer> wordStats) {
        MongoCollection<Document> collection = database.getCollection("saved_results");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        Document document = new Document()
                .append("date", date)
                .append("path", filePath)
                .append("result", wordStats);

        try {
            collection.insertOne(document);
        } catch (MongoCommandException e) {
            return false;
        }

        return true;
    }
}
