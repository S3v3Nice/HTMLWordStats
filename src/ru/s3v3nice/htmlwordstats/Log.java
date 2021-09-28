package ru.s3v3nice.htmlwordstats;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class Log implements ILog {
    private final Logger logger;

    public Log(String logFilePath) {
        logger = Logger.getLogger("");
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        try {
            File logFile = new File(logFilePath);
            FileHandler fh = new FileHandler(logFile.getPath(), true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            error("Ошибка при инициализации лог-файла.");
        }
    }

    /**
     * Логирует ошибку в консоль и лог-файл
     *
     * @param msg сообщение об ошибке
     */
    public void error(String msg) {
        System.out.println(msg);
        logger.severe(msg + "\n");
    }
}
