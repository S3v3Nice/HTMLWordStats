package ru.s3v3nice.htmlwordstats;

import ru.s3v3nice.htmlwordstats.database.MongoDB;
import ru.s3v3nice.htmlwordstats.html.HTMLTextTool;
import ru.s3v3nice.htmlwordstats.log.Log;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String logPath = Application.getDirectoryPath() + "log.txt";
        Application application = new Application(
                new Log(logPath),
                new HTMLTextTool(),
                new MongoDB("htmlwordstats")
        );
        String path;

        if (args.length > 0) {
            path = args[0];
        } else {
            System.out.print("Путь до html файла: ");
            Scanner scanner = new Scanner(System.in);
            path = scanner.nextLine();
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            application.logError("Неверный путь до html файла: " + path);
            return;
        }

        application.printWordStats(path);
    }
}
